'use client';

import { PaymentForm } from '@/components/payment/PaymentForm';
import { MainLayout } from '@/components/layout/MainLayout';
import { getOrder } from '@/services/order.service';
import { processPayment } from '@/services/payment.service';
import type { OrderDetail } from '@/types/order';
import { useRouter, useSearchParams } from 'next/navigation';
import { Suspense, useEffect, useState } from 'react';

function PaymentContent() {
  const params = useSearchParams();
  const router = useRouter();
  const orderId = Number(params.get('orderId'));
  const [order, setOrder] = useState<OrderDetail | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!orderId) return;
    getOrder(orderId)
      .then(setOrder)
      .finally(() => setLoading(false));
  }, [orderId]);

  async function handlePay(data: { method: 'CARD' | 'PAYPAL' | 'BANK_TRANSFER'; cardNumber?: string }) {
    const payment = await processPayment({
      orderId,
      method: data.method,
      cardNumber: data.cardNumber,
    });
    router.push(
      `/checkout/confirmation?orderId=${payment.orderId}&orderNumber=${payment.orderNumber}&paid=true`,
    );
  }

  if (loading) {
    return <p className="text-slate-600">Chargement...</p>;
  }

  if (!order) {
    return <p className="text-slate-600">Commande introuvable.</p>;
  }

  if (order.status !== 'PENDING') {
    return (
      <div className="rounded-2xl border border-slate-200 bg-white p-6 text-center">
        <p className="text-slate-600">Cette commande a déjà été payée.</p>
      </div>
    );
  }

  return <PaymentForm orderId={order.id} amount={order.totalAmount} onSubmit={handlePay} />;
}

export default function CheckoutPaymentPage() {
  return (
    <MainLayout>
      <div className="mx-auto max-w-md px-4 py-10">
        <h1 className="mb-8 text-2xl font-bold text-slate-900">Paiement</h1>
        <Suspense fallback={<p className="text-slate-600">Chargement...</p>}>
          <PaymentContent />
        </Suspense>
      </div>
    </MainLayout>
  );
}
