'use client';

import { ChartComponent } from '@/components/admin/ChartComponent';
import { StatCard } from '@/components/admin/StatCard';
import { getAdminAnalytics } from '@/services/admin.service';
import type { AdminAnalytics } from '@/types/admin';
import { useEffect, useState } from 'react';

export default function AdminStatisticsPage() {
  const [stats, setStats] = useState<AdminAnalytics | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getAdminAnalytics()
      .then(setStats)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p className="text-slate-600">Chargement...</p>;
  if (!stats) return <p className="text-red-600">Erreur de chargement.</p>;

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">Statistiques</h1>
      <div className="grid gap-4 sm:grid-cols-2">
        <StatCard label="Revenus" value={`${stats.totalRevenue.toFixed(2)} €`} />
        <StatCard label="Commissions perçues" value={`${stats.totalCommissions.toFixed(2)} €`} />
        <StatCard label="Retraits en attente" value={stats.pendingWithdrawals} />
        <StatCard label="Vendeurs en attente" value={stats.pendingSellers} />
      </div>
      <ChartComponent
        title="Répartition financière"
        data={[
          { label: 'Revenus', value: Number(stats.totalRevenue.toFixed(0)) },
          { label: 'Commissions', value: Number(stats.totalCommissions.toFixed(0)) },
          { label: 'Commandes', value: stats.totalOrders },
        ]}
      />
    </div>
  );
}
