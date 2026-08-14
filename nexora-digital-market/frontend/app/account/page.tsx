'use client';

import { getProfile } from '@/services/account.service';
import { getCart } from '@/services/cart.service';
import { getFavorites } from '@/services/favorite.service';
import type { UserProfile } from '@/types/client';
import Link from 'next/link';
import { useEffect, useState } from 'react';

export default function AccountPage() {
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [cartCount, setCartCount] = useState(0);
  const [favCount, setFavCount] = useState(0);

  useEffect(() => {
    async function load() {
      const [prof, cart, favs] = await Promise.all([getProfile(), getCart(), getFavorites()]);
      setProfile(prof);
      setCartCount(cart.itemCount);
      setFavCount(favs.length);
    }
    load();
  }, []);

  if (!profile) {
    return <p className="text-slate-600">Chargement...</p>;
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">Mon compte</h1>
      <p className="text-slate-600">
        Bienvenue, {profile.firstName ?? profile.email}
      </p>
      <div className="grid gap-4 sm:grid-cols-3">
        <Link href="/cart" className="rounded-2xl border border-slate-200 bg-white p-5 hover:border-slate-400">
          <p className="text-sm text-slate-500">Panier</p>
          <p className="mt-1 text-2xl font-bold">{cartCount}</p>
        </Link>
        <Link href="/account/favorites" className="rounded-2xl border border-slate-200 bg-white p-5 hover:border-slate-400">
          <p className="text-sm text-slate-500">Favoris</p>
          <p className="mt-1 text-2xl font-bold">{favCount}</p>
        </Link>
        <Link href="/account/orders" className="rounded-2xl border border-slate-200 bg-white p-5 hover:border-slate-400">
          <p className="text-sm text-slate-500">Commandes</p>
          <p className="mt-1 text-2xl font-bold">—</p>
        </Link>
      </div>
    </div>
  );
}
