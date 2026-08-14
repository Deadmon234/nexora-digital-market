'use client';

import { DataTable } from '@/components/admin/DataTable';
import { Button } from '@/components/ui/Button';
import { getAdminProducts, updateProductActive } from '@/services/admin.service';
import type { AdminProduct } from '@/types/admin';
import { useEffect, useState } from 'react';

export default function AdminProductsPage() {
  const [products, setProducts] = useState<AdminProduct[]>([]);
  const [loading, setLoading] = useState(true);

  async function load() {
    setLoading(true);
    try {
      setProducts(await getAdminProducts());
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
      <h1 className="text-2xl font-bold text-slate-900">Produits</h1>
      <DataTable
        data={products}
        columns={[
          { key: 'name', label: 'Nom' },
          { key: 'categoryName', label: 'Catégorie' },
          { key: 'brandName', label: 'Marque' },
          { key: 'offerCount', label: 'Offres' },
          { key: 'active', label: 'Actif', render: (row) => (row.active ? 'Oui' : 'Non') },
          {
            key: 'actions',
            label: 'Actions',
            render: (row) => (
              <Button
                type="button"
                variant="secondary"
                onClick={async () => { await updateProductActive(row.id, !row.active); load(); }}
              >
                {row.active ? 'Désactiver' : 'Activer'}
              </Button>
            ),
          },
        ]}
      />
    </div>
  );
}
