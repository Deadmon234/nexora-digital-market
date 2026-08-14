'use client';

import { OrderSummaryCard } from '@/components/order/OrderSummary';
import { OrderTimeline } from '@/components/order/OrderTimeline';
import { getOrder } from '@/services/order.service';
import type { OrderDetail } from '@/types/order';
import Link from 'next/link';
import { useParams } from 'next/navigation';
import { useEffect, useState } from 'react';

export default function AccountOrderDetailPage() {
  const params = useParams<{ id: string }>();
  const orderId = Number(params.id);
  const [order, setOrder] = useState<OrderDetail | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getOrder(orderId)
      .then(setOrder)
      .finally(() => setLoading(false));
  }, [orderId]);

  if (loading) {
    return <p className="text-slate-600">Chargement...</p>;
  }

  if (!order) {
    return <p className="text-slate-600">Commande introuvable.</p>;
  }

  return (
    <div className="space-y-6">
      <Link href="/account/orders" className="text-sm text-slate-600 hover:text-slate-900">
        ← Retour aux commandes
      </Link>
      <h1 className="text-2xl font-bold text-slate-900">Commande {order.orderNumber}</h1>

      <div className="grid gap-6 lg:grid-cols-2">
        <OrderSummaryCard order={order} showShipping shipping={order} />
        <div className="rounded-2xl border border-slate-200 bg-white p-6">
          <h2 className="text-lg font-semibold text-slate-900">Suivi</h2>
          <div className="mt-4">
            <OrderTimeline status={order.status} />
          </div>
        </div>
      </div>
    </div>
  );
}
