'use client';

import { DataTable } from '@/components/admin/DataTable';
import { Button } from '@/components/ui/Button';
import { getAdminWithdrawals, updateWithdrawalStatus } from '@/services/admin.service';
import type { AdminWithdrawal } from '@/types/admin';
import { useEffect, useState } from 'react';

export default function AdminWithdrawalsPage() {
  const [withdrawals, setWithdrawals] = useState<AdminWithdrawal[]>([]);
  const [loading, setLoading] = useState(true);

  async function load() {
    setLoading(true);
    try {
      setWithdrawals(await getAdminWithdrawals());
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  if (loading) return <p className="text-slate-600">Chargement...</p>;

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">Retraits</h1>
      <DataTable
        data={withdrawals}
        columns={[
          { key: 'sellerCompanyName', label: 'Vendeur' },
          { key: 'amount', label: 'Montant', render: (row) => `${row.amount.toFixed(2)} €` },
          { key: 'bankAccount', label: 'IBAN' },
          { key: 'status', label: 'Statut' },
          {
            key: 'actions',
            label: 'Actions',
            render: (row) =>
              row.status === 'PENDING' ? (
                <div className="flex gap-2">
                  <Button type="button" onClick={async () => { await updateWithdrawalStatus(row.id, 'PAID'); load(); }}>
                    Valider
                  </Button>
                  <Button type="button" variant="secondary" onClick={async () => { await updateWithdrawalStatus(row.id, 'REJECTED'); load(); }}>
                    Rejeter
                  </Button>
                </div>
              ) : null,
          },
        ]}
      />
    </div>
  );
}
