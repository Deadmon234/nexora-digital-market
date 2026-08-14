'use client';

import { ChartComponent } from '@/components/admin/ChartComponent';
import { StatCard } from '@/components/admin/StatCard';
import { getAdminAnalytics } from '@/services/admin.service';
import type { AdminAnalytics } from '@/types/admin';
import { useEffect, useState } from 'react';

export default function AdminDashboardPage() {
  const [stats, setStats] = useState<AdminAnalytics | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getAdminAnalytics()
      .then(setStats)
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return <p className="text-slate-600">Chargement...</p>;
  }

  if (!stats) {
    return <p className="text-red-600">Impossible de charger les statistiques.</p>;
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-slate-900">Administration Nexora</h1>
        <p className="mt-1 text-sm text-slate-600">Vue d&apos;ensemble de la plateforme</p>
      </div>

      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <StatCard label="Utilisateurs" value={stats.totalUsers} />
        <StatCard label="Vendeurs" value={stats.totalSellers} hint={`${stats.pendingSellers} en attente`} />
        <StatCard label="Produits" value={stats.totalProducts} />
        <StatCard label="Commandes" value={stats.totalOrders} hint={`${stats.pendingOrders} en attente`} />
      </div>

      <div className="grid gap-4 lg:grid-cols-2">
        <StatCard label="Revenus totaux" value={`${stats.totalRevenue.toFixed(2)} €`} />
        <StatCard label="Commissions" value={`${stats.totalCommissions.toFixed(2)} €`} />
      </div>

      <ChartComponent
        title="Activité plateforme"
        data={[
          { label: 'Utilisateurs', value: stats.totalUsers },
          { label: 'Vendeurs', value: stats.totalSellers },
          { label: 'Produits', value: stats.totalProducts },
          { label: 'Commandes', value: stats.totalOrders },
        ]}
      />
    </div>
  );
}
