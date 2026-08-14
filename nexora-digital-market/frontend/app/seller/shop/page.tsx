'use client';

import { Button } from '@/components/ui/Button';
import { ApiError } from '@/services/api.service';
import { getMyShop, updateMyShop } from '@/services/seller.service';
import type { Shop } from '@/types/seller';
import { FormEvent, useEffect, useState } from 'react';

export default function SellerShopPage() {
  const [shop, setShop] = useState<Shop | null>(null);
  const [isNew, setIsNew] = useState(false);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [form, setForm] = useState({ name: '', description: '', logoUrl: '', bannerUrl: '' });

  useEffect(() => {
    async function load() {
      try {
        const data = await getMyShop();
        setShop(data);
        setForm({
          name: data.name,
          description: data.description ?? '',
          logoUrl: data.logoUrl ?? '',
          bannerUrl: data.bannerUrl ?? '',
        });
      } catch (err) {
        if (err instanceof ApiError && err.status === 404) {
          setIsNew(true);
        }
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setSaving(true);
    setError('');
    try {
      const saved = await updateMyShop(form);
      setShop(saved);
      setIsNew(false);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erreur');
    } finally {
      setSaving(false);
    }
  }

  if (loading) {
    return <p className="text-slate-600">Chargement...</p>;
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">
        {isNew ? 'Créer ma boutique' : 'Ma boutique'}
      </h1>

      {shop && !isNew && (
        <div className="rounded-xl border border-slate-200 bg-white p-4 text-sm text-slate-600">
          Slug : <span className="font-medium text-slate-900">{shop.slug}</span> — Statut :{' '}
          {shop.status}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4 rounded-2xl border border-slate-200 bg-white p-6">
        <div>
          <label className="block text-sm font-medium">Nom de la boutique</label>
          <input
            required
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm"
          />
        </div>
        <div>
          <label className="block text-sm font-medium">Description</label>
          <textarea
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
            className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm"
            rows={4}
          />
        </div>
        <div>
          <label className="block text-sm font-medium">Logo URL</label>
          <input
            value={form.logoUrl}
            onChange={(e) => setForm({ ...form, logoUrl: e.target.value })}
            className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm"
          />
        </div>
        <div>
          <label className="block text-sm font-medium">Bannière URL</label>
          <input
            value={form.bannerUrl}
            onChange={(e) => setForm({ ...form, bannerUrl: e.target.value })}
            className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm"
          />
        </div>
        {error && <p className="text-sm text-red-600">{error}</p>}
        <Button type="submit" disabled={saving}>
          {saving ? 'Enregistrement...' : isNew ? 'Créer la boutique' : 'Enregistrer'}
        </Button>
      </form>
    </div>
  );
}
