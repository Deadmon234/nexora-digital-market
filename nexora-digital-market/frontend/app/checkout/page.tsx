'use client';

import { CheckoutForm } from '@/components/client/CheckoutForm';
import { MainLayout } from '@/components/layout/MainLayout';
import { getAddresses } from '@/services/account.service';
import { getCart } from '@/services/cart.service';
import { createOrder } from '@/services/order.service';
import type { Address, Cart } from '@/types/client';
import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';

export default function CheckoutPage() {
  const router = useRouter();
  const [cart, setCart] = useState<Cart | null>(null);
  const [addresses, setAddresses] = useState<Address[]>([]);
  const [selectedAddressId, setSelectedAddressId] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    async function load() {
      const [cartData, addressData] = await Promise.all([getCart(), getAddresses()]);
      setCart(cartData);
      setAddresses(addressData);
      const defaultAddr = addressData.find((a) => a.defaultAddress) ?? addressData[0];
      if (defaultAddr) setSelectedAddressId(defaultAddr.id);
      setLoading(false);
    }
    load();
  }, []);

  async function handleSubmit() {
    if (!selectedAddressId) return;
    setSubmitting(true);
    setError('');
    try {
      const order = await createOrder(selectedAddressId);
      router.push(`/checkout/payment?orderId=${order.id}`);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erreur lors de la commande');
      setSubmitting(false);
    }
  }

  if (loading || !cart) {
    return (
      <MainLayout>
        <div className="mx-auto max-w-3xl px-4 py-10">
          <p className="text-slate-600">Chargement...</p>
        </div>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <div className="mx-auto max-w-3xl px-4 py-10">
        <h1 className="mb-8 text-2xl font-bold text-slate-900">Checkout</h1>
        {error && <p className="mb-4 text-sm text-red-600">{error}</p>}
        <CheckoutForm
          cart={cart}
          addresses={addresses}
          selectedAddressId={selectedAddressId}
          onSelectAddress={setSelectedAddressId}
          onSubmit={handleSubmit}
          loading={submitting}
        />
      </div>
    </MainLayout>
  );
}
