'use client';

import { OrderSummaryCard } from '@/components/order/OrderSummary';
import { OrderStatusBadge } from '@/components/order/OrderStatusBadge';
import { Button } from '@/components/ui/Button';
import { getSellerOrder, updateSellerOrderStatus } from '@/services/order.service';
import type { OrderStatus, SellerOrder } from '@/types/order';
import Link from 'next/link';
import { useParams } from 'next/navigation';
import { useEffect, useState } from 'react';

const NEXT_STATUS: Partial<Record<OrderStatus, OrderStatus>> = {
  CONFIRMED: 'PROCESSING',
  PROCESSING: 'SHIPPED',
  SHIPPED: 'DELIVERED',
};

export default function SellerOrderDetailPage() {
  const params = useParams<{ id: string }>();
  const orderId = Number(params.id);
  const [order, setOrder] = useState<SellerOrder | null>(null);
  const [loading, setLoading] = useState(true);
  const [updating, setUpdating] = useState(false);

  async function load() {
    setLoading(true);
    try {
      setOrder(await getSellerOrder(orderId));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, [orderId]);

  async function advanceStatus() {
    if (!order) return;
    const next = NEXT_STATUS[order.status];
    if (!next) return;
    setUpdating(true);
    try {
      setOrder(await updateSellerOrderStatus(orderId, next));
    } finally {
      setUpdating(false);
    }
  }

  if (loading) {
    return <p className="text-slate-600">Chargement...</p>;
  }

  if (!order) {
    return <p className="text-slate-600">Commande introuvable.</p>;
  }

  const nextStatus = NEXT_STATUS[order.status];

  return (
    <div className="space-y-6">
      <Link href="/seller/orders" className="text-sm text-slate-600 hover:text-slate-900">
        ← Retour aux commandes
      </Link>
      <div className="flex flex-wrap items-center gap-4">
        <h1 className="text-2xl font-bold text-slate-900">Commande #{order.id}</h1>
        <OrderStatusBadge status={order.status} />
      </div>

      <OrderSummaryCard order={order} />

      {nextStatus && (
        <Button type="button" disabled={updating} onClick={advanceStatus}>
          {updating ? 'Mise à jour...' : `Passer en ${nextStatus}`}
        </Button>
      )}
    </div>
  );
}
