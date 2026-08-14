'use client';

import { DataTable } from '@/components/admin/DataTable';
import { Button } from '@/components/ui/Button';
import { createCategory, getAdminCategories } from '@/services/admin.service';
import type { AdminCategory } from '@/types/admin';
import { FormEvent, useEffect, useState } from 'react';

export default function AdminCategoriesPage() {
  const [categories, setCategories] = useState<AdminCategory[]>([]);
  const [name, setName] = useState('');
  const [loading, setLoading] = useState(true);

  async function load() {
    setLoading(true);
    try {
      setCategories(await getAdminCategories());
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function handleCreate(e: FormEvent) {
    e.preventDefault();
    await createCategory({ name });
    setName('');
    load();
  }

  if (loading) return <p className="text-slate-600">Chargement...</p>;

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">Catégories</h1>
      <form onSubmit={handleCreate} className="flex flex-wrap gap-2">
        <input
          required
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Nom de la catégorie"
          className="rounded-lg border border-slate-300 px-3 py-2 text-sm"
        />
        <Button type="submit">Ajouter</Button>
      </form>
      <DataTable
        data={categories}
        columns={[
          { key: 'name', label: 'Nom' },
          { key: 'slug', label: 'Slug' },
          { key: 'active', label: 'Active', render: (row) => (row.active ? 'Oui' : 'Non') },
        ]}
      />
    </div>
  );
}
