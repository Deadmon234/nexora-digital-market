'use client';

import { CartItemRow } from '@/components/client/CartItem';
import { CartSummary } from '@/components/client/CartSummary';
import { MainLayout } from '@/components/layout/MainLayout';
import { getCart, removeCartItem, updateCartItem } from '@/services/cart.service';
import type { Cart } from '@/types/client';
import { useEffect, useState } from 'react';

export default function CartPage() {
  const [cart, setCart] = useState<Cart | null>(null);
  const [loading, setLoading] = useState(true);
  const [updating, setUpdating] = useState(false);

  async function load() {
    setLoading(true);
    try {
      setCart(await getCart());
    } catch {
      setCart({ id: 0, items: [], itemCount: 0, totalAmount: 0 });
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function handleUpdate(itemId: number, quantity: number) {
    if (quantity < 1) return;
    setUpdating(true);
    try {
      setCart(await updateCartItem(itemId, quantity));
    } finally {
      setUpdating(false);
    }
  }

  async function handleRemove(itemId: number) {
    setUpdating(true);
    try {
      setCart(await removeCartItem(itemId));
    } finally {
      setUpdating(false);
    }
  }

  return (
    <MainLayout>
      <div className="mx-auto max-w-6xl px-4 py-10">
        <h1 className="text-2xl font-bold text-slate-900">Mon panier</h1>

        {loading || !cart ? (
          <p className="mt-6 text-slate-600">Chargement...</p>
        ) : cart.items.length === 0 ? (
          <p className="mt-6 text-slate-600">Votre panier est vide.</p>
        ) : (
          <div className="mt-8 grid gap-8 lg:grid-cols-3">
            <div className="space-y-4 lg:col-span-2">
              {cart.items.map((item) => (
                <CartItemRow
                  key={item.id}
                  item={item}
                  loading={updating}
                  onUpdateQuantity={handleUpdate}
                  onRemove={handleRemove}
                />
              ))}
            </div>
            <CartSummary itemCount={cart.itemCount} totalAmount={cart.totalAmount} />
          </div>
        )}
      </div>
    </MainLayout>
  );
}
