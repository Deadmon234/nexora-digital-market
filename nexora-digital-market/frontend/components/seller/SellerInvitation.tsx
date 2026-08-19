'use client';

import { useAuth } from '@/components/providers/AuthProvider';
import Link from 'next/link';

export function SellerInvitation() {
  const { user, isLoading } = useAuth();

  if (isLoading || !user || user.roles.includes('ROLE_SELLER') || user.roles.includes('ROLE_ADMIN')) {
    return null;
  }

  return (
    <section className="relative overflow-hidden rounded-2xl bg-nexora-navy px-6 py-8 text-white shadow-lg sm:px-8">
      <div className="absolute -right-12 -top-16 h-44 w-44 rounded-full border-[24px] border-nexora-green/30" />
      <div className="relative flex flex-col gap-6 sm:flex-row sm:items-center sm:justify-between">
        <div className="max-w-xl">
          <p className="text-xs font-bold uppercase tracking-[0.2em] text-nexora-green-bright">Une nouvelle aventure</p>
          <h2 className="display-font mt-2 text-3xl">Vous souhaitez devenir vendeur ?</h2>
          <p className="mt-3 text-sm leading-6 text-slate-300">
            Rejoignez Nexora, créez votre boutique et proposez vos produits à une communauté de clients engagés.
          </p>
        </div>
        <Link
          href="/become-seller"
          className="inline-flex shrink-0 items-center justify-center rounded-full bg-nexora-green px-5 py-3 text-sm font-bold text-nexora-navy transition-colors hover:bg-nexora-green-bright"
        >
          Devenir vendeur <span className="ml-2">↗</span>
        </Link>
      </div>
    </section>
  );
}
