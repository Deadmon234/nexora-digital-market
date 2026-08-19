'use client';

import { DashboardMetrics } from '@/components/seller/DashboardMetrics';
import { Button } from '@/components/ui/Button';
import { ApiError } from '@/services/api.service';
import { refreshAccessToken } from '@/services/auth.service';
import { applyAsSeller, getSellerDashboard, getSellerProfile } from '@/services/seller.service';
import type { SellerDashboard, SellerProfile } from '@/types/seller';
import { useAuth } from '@/components/providers/AuthProvider';
import Link from 'next/link';
import { FormEvent, useEffect, useState } from 'react';

export default function SellerDashboardPage() {
  const { setUser } = useAuth();
  const [dashboard, setDashboard] = useState<SellerDashboard | null>(null);
  const [profile, setProfile] = useState<SellerProfile | null>(null);
  const [needsApply, setNeedsApply] = useState(false);
  const [loading, setLoading] = useState(true);
  const [companyName, setCompanyName] = useState('');
  const [applyError, setApplyError] = useState('');
  const [applying, setApplying] = useState(false);

  useEffect(() => {
    async function load() {
      try {
        const [dash, prof] = await Promise.all([getSellerDashboard(), getSellerProfile()]);
        setDashboard(dash);
        setProfile(prof);
      } catch (err) {
        if (err instanceof ApiError && (err.status === 403 || err.status === 401)) {
          setNeedsApply(true);
        }
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  async function handleApply(e: FormEvent) {
    e.preventDefault();
    setApplying(true);
    setApplyError('');
    try {
      const result = await applyAsSeller({ companyName });
      setProfile(result);
      setNeedsApply(false);
      const refreshed = await refreshAccessToken();
      if (refreshed) {
        setUser(refreshed.user);
      }
      const dash = await getSellerDashboard();
      setDashboard(dash);
    } catch (err) {
      setApplyError(err instanceof Error ? err.message : 'Erreur');
    } finally {
      setApplying(false);
    }
  }

  if (loading) {
    return <div className="rounded-2xl border border-slate-200 bg-white p-8 text-slate-600 shadow-sm">Chargement de votre espace vendeur...</div>;
  }

  if (needsApply) {
    return (
      <div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm sm:p-8">
        <p className="text-xs font-bold uppercase tracking-[0.2em] text-nexora-coral">Première étape</p>
        <h1 className="mt-2 text-3xl font-bold text-nexora-navy">Configurez votre activité</h1>
        <p className="mt-3 max-w-xl text-sm leading-6 text-slate-600">
          Postulez pour accéder à l&apos;espace vendeur Nexora.
        </p>
        <form onSubmit={handleApply} className="mt-8 max-w-md space-y-4">
          <div>
            <label className="block text-sm font-medium">Nom de l&apos;entreprise</label>
            <input
              required
              value={companyName}
              onChange={(e) => setCompanyName(e.target.value)}
              className="mt-2 w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm focus:border-nexora-blue focus:bg-white focus:outline-none focus:ring-4 focus:ring-nexora-blue/10"
            />
          </div>
          {applyError && <p className="text-sm text-red-600">{applyError}</p>}
          <Button type="submit" disabled={applying} className="rounded-xl px-5 py-3">
            {applying ? 'Envoi...' : 'Postuler'}
          </Button>
        </form>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <section className="relative overflow-hidden rounded-2xl bg-nexora-navy p-6 text-white shadow-lg sm:p-8">
        <div className="absolute -right-12 -top-20 h-56 w-56 rounded-full border-[30px] border-nexora-green/20" />
        <div className="relative flex flex-col gap-6 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <p className="text-xs font-bold uppercase tracking-[0.2em] text-nexora-green-bright">Vue d&apos;ensemble</p>
          <h1 className="mt-2 text-3xl font-bold">Bonjour, bienvenue dans votre boutique</h1>
          {profile && (
            <p className="mt-3 text-sm text-slate-300">
              {profile.companyName || 'Votre entreprise'} <span className="mx-2 text-white/40">•</span> statut {profile.status}
            </p>
          )}
        </div>
        <Link href="/seller/products/new" className="relative">
          <Button className="rounded-xl bg-nexora-green px-5 py-3 font-bold text-nexora-navy hover:bg-nexora-green-bright">Ajouter un produit <span className="ml-2">+</span></Button>
        </Link>
      </div>
      </section>

      {dashboard && <DashboardMetrics data={dashboard} />}

      <section className="grid gap-6 lg:grid-cols-[1.4fr_0.6fr]">
        <div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-xs font-bold uppercase tracking-[0.16em] text-slate-400">Prochaines actions</p>
              <h2 className="mt-1 text-xl font-bold text-nexora-navy">Faites avancer votre boutique</h2>
            </div>
            <span className="rounded-full bg-nexora-green/15 px-3 py-1 text-xs font-bold text-nexora-teal">À faire</span>
          </div>
          <div className="mt-6 grid gap-3 sm:grid-cols-3">
            <Link href="/seller/shop" className="rounded-xl border border-slate-200 p-4 transition-colors hover:border-nexora-blue hover:bg-nexora-blue/5">
              <p className="text-sm font-bold text-nexora-navy">Personnaliser la boutique</p>
              <p className="mt-2 text-xs leading-5 text-slate-500">Ajoutez votre identité et vos informations.</p>
            </Link>
            <Link href="/seller/products/new" className="rounded-xl border border-slate-200 p-4 transition-colors hover:border-nexora-blue hover:bg-nexora-blue/5">
              <p className="text-sm font-bold text-nexora-navy">Ajouter un produit</p>
              <p className="mt-2 text-xs leading-5 text-slate-500">Présentez votre première offre aux clients.</p>
            </Link>
            <Link href="/seller/inventory" className="rounded-xl border border-slate-200 p-4 transition-colors hover:border-nexora-blue hover:bg-nexora-blue/5">
              <p className="text-sm font-bold text-nexora-navy">Vérifier le stock</p>
              <p className="mt-2 text-xs leading-5 text-slate-500">Gardez vos produits disponibles.</p>
            </Link>
          </div>
        </div>
        <div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
          <p className="text-xs font-bold uppercase tracking-[0.16em] text-slate-400">Accès rapide</p>
          <div className="mt-5 space-y-3">
            <Link href="/seller/orders" className="flex items-center justify-between rounded-xl bg-slate-50 px-4 py-3 text-sm font-bold text-nexora-navy hover:bg-nexora-blue/5">Commandes reçues <span>→</span></Link>
            <Link href="/seller/revenues" className="flex items-center justify-between rounded-xl bg-slate-50 px-4 py-3 text-sm font-bold text-nexora-navy hover:bg-nexora-blue/5">Voir mes revenus <span>→</span></Link>
            <Link href="/seller/withdrawals" className="flex items-center justify-between rounded-xl bg-slate-50 px-4 py-3 text-sm font-bold text-nexora-navy hover:bg-nexora-blue/5">Gérer les retraits <span>→</span></Link>
          </div>
        </div>
      </section>

      {profile?.status === 'PENDING' && (
        <div className="rounded-2xl border border-amber-200 bg-amber-50 p-5 text-sm text-amber-800">
          Votre compte vendeur est en attente de validation par un administrateur.
        </div>
      )}
    </div>
  );
}
