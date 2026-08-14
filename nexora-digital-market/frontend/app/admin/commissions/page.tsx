'use client';

import { DataTable } from '@/components/admin/DataTable';
import { getAdminCommissions } from '@/services/admin.service';
import type { AdminCommission } from '@/types/admin';
import { useEffect, useState } from 'react';

export default function AdminCommissionsPage() {
  const [commissions, setCommissions] = useState<AdminCommission[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getAdminCommissions()
      .then(setCommissions)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p className="text-slate-600">Chargement...</p>;

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">Commissions</h1>
      <DataTable
        data={commissions}
        columns={[
          { key: 'orderNumber', label: 'Commande' },
          { key: 'sellerCompanyName', label: 'Vendeur' },
          { key: 'orderAmount', label: 'Montant', render: (row) => `${row.orderAmount.toFixed(2)} €` },
          { key: 'commissionRate', label: 'Taux %' },
          { key: 'commissionAmount', label: 'Commission', render: (row) => `${row.commissionAmount.toFixed(2)} €` },
          { key: 'sellerAmount', label: 'Vendeur', render: (row) => `${row.sellerAmount.toFixed(2)} €` },
        ]}
      />
    </div>
  );
}
