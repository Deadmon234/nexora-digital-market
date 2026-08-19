'use client';

import { MainLayout } from '@/components/layout/MainLayout';
import { useAuth } from '@/components/providers/AuthProvider';
import { Button } from '@/components/ui/Button';
import { applyAsSeller } from '@/services/seller.service';
import { refreshAccessToken } from '@/services/auth.service';
import { useRouter } from 'next/navigation';
import { FormEvent, useEffect, useState } from 'react';

export default function BecomeSellerPage() {
  const { user, isLoading, setUser } = useAuth();
  const router = useRouter();
  const [companyName, setCompanyName] = useState('');
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (user?.roles.includes('ROLE_ADMIN')) {
      router.replace('/admin');
    } else if (user?.roles.includes('ROLE_SELLER')) {
      router.replace('/seller');
    }
  }, [router, user]);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setError('');
    setSubmitting(true);

    try {
      await applyAsSeller({ companyName: companyName.trim() });
      const refreshed = await refreshAccessToken();
      if (refreshed) setUser(refreshed.user);
      router.push('/seller');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Impossible d’envoyer votre demande.');
    } finally {
      setSubmitting(false);
    }
  }

  if (isLoading) {
    return <MainLayout><div className="mx-auto max-w-2xl px-4 py-12">Chargement...</div></MainLayout>;
  }

  if (!user) {
    return (
      <MainLayout>
        <div className="mx-auto max-w-2xl px-4 py-12 text-center">
          <h1 className="text-2xl font-bold text-nexora-navy">Connectez-vous pour devenir vendeur</h1>
          <Button className="mt-6" onClick={() => router.push('/auth/login')}>Se connecter</Button>
        </div>
      </MainLayout>
    );
  }

  if (user.roles.includes('ROLE_SELLER') || user.roles.includes('ROLE_ADMIN')) {
    return <MainLayout><div className="mx-auto max-w-2xl px-4 py-12">Redirection...</div></MainLayout>;
  }

  return (
    <MainLayout>
      <div className="mx-auto max-w-2xl px-4 py-12">
        <div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm sm:p-8">
          <p className="text-xs font-bold uppercase tracking-[0.2em] text-nexora-coral">Devenir partenaire Nexora</p>
          <h1 className="display-font mt-2 text-4xl text-nexora-navy">Créez votre boutique</h1>
          <p className="mt-3 text-sm leading-6 text-slate-600">
            Indiquez le nom de votre entreprise pour envoyer votre demande à notre équipe.
          </p>
          <form onSubmit={handleSubmit} className="mt-8 space-y-4">
            <div>
              <label htmlFor="companyName" className="block text-sm font-medium text-slate-700">Nom de l’entreprise</label>
              <input
                id="companyName"
                required
                value={companyName}
                onChange={(event) => setCompanyName(event.target.value)}
                className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm focus:border-nexora-blue focus:outline-none focus:ring-2 focus:ring-nexora-blue/20"
              />
            </div>
            {error && <p className="text-sm text-red-600">{error}</p>}
            <Button type="submit" disabled={submitting}>
              {submitting ? 'Envoi de la demande...' : 'Envoyer ma demande'}
            </Button>
          </form>
        </div>
      </div>
    </MainLayout>
  );
}
