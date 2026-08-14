'use client';

import { OrderStatusBadge } from '@/components/order/OrderStatusBadge';
import { formatPrice } from '@/services/catalog.service';
import { getOrders } from '@/services/order.service';
import type { OrderSummary } from '@/types/order';
import Link from 'next/link';
import { useEffect, useState } from 'react';

export default function AccountOrdersPage() {
  const [orders, setOrders] = useState<OrderSummary[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getOrders()
      .then(setOrders)
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return <p className="text-slate-600">Chargement...</p>;
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">Mes commandes</h1>

      {orders.length === 0 ? (
        <p className="text-slate-600">Aucune commande pour le moment.</p>
      ) : (
        <ul className="space-y-3">
          {orders.map((order) => (
            <li key={order.id}>
              <Link
                href={`/account/orders/${order.id}`}
                className="flex flex-col gap-3 rounded-xl border border-slate-200 bg-white p-4 hover:border-slate-400 sm:flex-row sm:items-center sm:justify-between"
              >
                <div>
                  <p className="font-medium text-slate-900">{order.orderNumber}</p>
                  <p className="text-sm text-slate-500">
                    {order.itemCount} article(s) · {order.sellerCount} vendeur(s)
                  </p>
                </div>
                <div className="flex items-center gap-4">
                  <OrderStatusBadge status={order.status} />
                  <span className="font-semibold">{formatPrice(order.totalAmount)}</span>
                </div>
              </Link>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
