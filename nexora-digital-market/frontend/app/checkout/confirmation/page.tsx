'use client';

import { MainLayout } from '@/components/layout/MainLayout';
import { SellerInvitation } from '@/components/seller/SellerInvitation';
import { Button } from '@/components/ui/Button';
import Link from 'next/link';
import { Suspense } from 'react';
import { useSearchParams } from 'next/navigation';

function ConfirmationContent() {
  const params = useSearchParams();
  const orderNumber = params.get('orderNumber') ?? '';
  const orderId = params.get('orderId');

  return (
    <div className="rounded-2xl border border-slate-200 bg-white p-8 text-center">
      <h1 className="text-2xl font-bold text-slate-900">Commande confirmée</h1>
      {orderNumber && (
        <p className="mt-4 text-slate-600">
          Numéro de commande : <span className="font-semibold text-slate-900">{orderNumber}</span>
        </p>
      )}
      <p className="mt-2 text-sm text-slate-500">
        Vous recevrez une notification lorsque vos vendeurs prépareront votre commande.
      </p>
      <div className="mt-8 flex flex-col gap-3 sm:flex-row sm:justify-center">
        {orderId && (
          <Link href={`/account/orders/${orderId}`}>
            <Button>Voir ma commande</Button>
          </Link>
        )}
        <Link href="/products">
          <Button variant="outline">Continuer mes achats</Button>
        </Link>
      </div>
    </div>
  );
}

export default function CheckoutConfirmationPage() {
  return (
    <MainLayout>
      <div className="mx-auto max-w-3xl px-4 py-10">
        <Suspense fallback={<p className="text-slate-600">Chargement...</p>}>
          <ConfirmationContent />
        </Suspense>
        <div className="mt-8">
          <SellerInvitation />
        </div>
      </div>
    </MainLayout>
  );
}
