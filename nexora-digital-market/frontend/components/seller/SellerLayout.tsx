'use client';

import { Navbar } from '@/components/layout/Navbar';
import { SellerSidebar } from '@/components/seller/SellerSidebar';
import { useAuth } from '@/components/providers/AuthProvider';
import Link from 'next/link';

export function SellerLayout({ children }: { children: React.ReactNode }) {
  const { user, isLoading } = useAuth();
  const isSeller = user?.roles.includes('ROLE_SELLER');

  if (isLoading) {
    return <div className="min-h-screen bg-gray-50" />;
  }

  if (!isSeller) {
    return (
      <div className="min-h-screen bg-gray-50">
        <Navbar />
        <div className="mx-auto max-w-6xl px-4 py-16 text-center">
          <h1 className="text-2xl font-bold text-slate-900">Accès refusé</h1>
          <p className="mt-2 text-slate-600">Cette section est réservée aux vendeurs.</p>
          <Link href="/" className="mt-4 inline-block text-nexora-blue hover:underline">
            Retour à l&apos;accueil
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#f3f6fa]">
      <Navbar />
      <div className="mx-auto flex max-w-7xl flex-col gap-6 px-4 py-6 sm:px-6 lg:flex-row lg:px-8 lg:py-8">
        <SellerSidebar />
        <main className="min-w-0 flex-1">{children}</main>
      </div>
    </div>
  );
}
