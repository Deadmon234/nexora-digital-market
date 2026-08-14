'use client';

import { DataTable } from '@/components/admin/DataTable';
import { Button } from '@/components/ui/Button';
import { getAdminSellers, updateSellerCommissionRate, updateSellerStatus } from '@/services/admin.service';
import type { AdminSeller } from '@/types/admin';
import { useEffect, useState } from 'react';

export default function AdminSellersPage() {
  const [sellers, setSellers] = useState<AdminSeller[]>([]);
  const [loading, setLoading] = useState(true);

  async function load() {
    setLoading(true);
    try {
      setSellers(await getAdminSellers());
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function approve(id: number) {
    await updateSellerStatus(id, 'APPROVED');
    load();
  }

  async function reject(id: number) {
    await updateSellerStatus(id, 'REJECTED');
    load();
  }

  async function setCommission(id: number, rate: number) {
    await updateSellerCommissionRate(id, rate);
    load();
  }

  if (loading) return <p className="text-slate-600">Chargement...</p>;

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">Vendeurs</h1>
      <DataTable
        data={sellers}
        columns={[
          { key: 'companyName', label: 'Entreprise' },
          { key: 'email', label: 'Email' },
          { key: 'status', label: 'Statut' },
          { key: 'commissionRate', label: 'Commission %' },
          {
            key: 'actions',
            label: 'Actions',
            render: (row) => (
              <div className="flex flex-wrap gap-2">
                {row.status === 'PENDING' && (
                  <>
                    <Button type="button" onClick={() => approve(row.id)}>Approuver</Button>
                    <Button type="button" variant="secondary" onClick={() => reject(row.id)}>Rejeter</Button>
                  </>
                )}
                <Button type="button" variant="secondary" onClick={() => setCommission(row.id, 15)}>
                  15%
                </Button>
              </div>
            ),
          },
        ]}
      />
    </div>
  );
}
