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
    return <p className="text-slate-600">Chargement...</p>;
  }

  if (needsApply) {
    return (
      <div className="rounded-2xl border border-slate-200 bg-white p-6">
        <h1 className="text-2xl font-bold text-slate-900">Devenir vendeur</h1>
        <p className="mt-2 text-sm text-slate-600">
          Postulez pour accéder à l&apos;espace vendeur Nexora.
        </p>
        <form onSubmit={handleApply} className="mt-6 space-y-4 max-w-md">
          <div>
            <label className="block text-sm font-medium">Nom de l&apos;entreprise</label>
            <input
              required
              value={companyName}
              onChange={(e) => setCompanyName(e.target.value)}
              className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm"
            />
          </div>
          {applyError && <p className="text-sm text-red-600">{applyError}</p>}
          <Button type="submit" disabled={applying}>
            {applying ? 'Envoi...' : 'Postuler'}
          </Button>
        </form>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Espace vendeur</h1>
          {profile && (
            <p className="mt-1 text-sm text-slate-600">
              {profile.companyName} — statut {profile.status}
            </p>
          )}
        </div>
        <Link href="/seller/products/new">
          <Button>Ajouter un produit</Button>
        </Link>
      </div>

      {dashboard && <DashboardMetrics data={dashboard} />}

      {profile?.status === 'PENDING' && (
        <div className="rounded-xl border border-amber-200 bg-amber-50 p-4 text-sm text-amber-800">
          Votre compte vendeur est en attente de validation par un administrateur.
        </div>
      )}
    </div>
  );
}
