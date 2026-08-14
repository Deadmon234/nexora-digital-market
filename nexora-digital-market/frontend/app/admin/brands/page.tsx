'use client';

import { DataTable } from '@/components/admin/DataTable';
import { Button } from '@/components/ui/Button';
import { createBrand, getAdminBrands } from '@/services/admin.service';
import type { AdminBrand } from '@/types/admin';
import { FormEvent, useEffect, useState } from 'react';

export default function AdminBrandsPage() {
  const [brands, setBrands] = useState<AdminBrand[]>([]);
  const [name, setName] = useState('');
  const [loading, setLoading] = useState(true);

  async function load() {
    setLoading(true);
    try {
      setBrands(await getAdminBrands());
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function handleCreate(e: FormEvent) {
    e.preventDefault();
    await createBrand({ name });
    setName('');
    load();
  }

  if (loading) return <p className="text-slate-600">Chargement...</p>;

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">Marques</h1>
      <form onSubmit={handleCreate} className="flex flex-wrap gap-2">
        <input
          required
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Nom de la marque"
          className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
        />
        <Button type="submit">Ajouter</Button>
      </form>
      <DataTable
        data={brands}
        columns={[
          { key: 'name', label: 'Nom' },
          { key: 'slug', label: 'Slug' },
          { key: 'active', label: 'Active', render: (row) => (row.active ? 'Oui' : 'Non') },
        ]}
      />
    </div>
  );
}
