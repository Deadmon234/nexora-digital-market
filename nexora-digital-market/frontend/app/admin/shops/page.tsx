'use client';

import { DataTable } from '@/components/admin/DataTable';
import { Button } from '@/components/ui/Button';
import { getAdminShops, updateShopActive, updateShopStatus } from '@/services/admin.service';
import type { AdminShop } from '@/types/admin';
import { useEffect, useState } from 'react';

export default function AdminShopsPage() {
  const [shops, setShops] = useState<AdminShop[]>([]);
  const [loading, setLoading] = useState(true);

  async function load() {
    setLoading(true);
    try {
      setShops(await getAdminShops());
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
      <h1 className="text-2xl font-bold text-slate-900">Boutiques</h1>
      <DataTable
        data={shops}
        columns={[
          { key: 'name', label: 'Nom' },
          { key: 'sellerCompanyName', label: 'Vendeur' },
          { key: 'status', label: 'Statut' },
          { key: 'active', label: 'Active', render: (row) => (row.active ? 'Oui' : 'Non') },
          {
            key: 'actions',
            label: 'Actions',
            render: (row) => (
              <div className="flex flex-wrap gap-2">
                {row.status === 'PENDING' && (
                  <Button type="button" onClick={async () => { await updateShopStatus(row.id, 'APPROVED'); load(); }}>
                    Approuver
                  </Button>
                )}
                <Button
                  type="button"
                  variant="secondary"
                  onClick={async () => { await updateShopActive(row.id, !row.active); load(); }}
                >
                  {row.active ? 'Désactiver' : 'Activer'}
                </Button>
              </div>
            ),
          },
        ]}
      />
    </div>
  );
}
