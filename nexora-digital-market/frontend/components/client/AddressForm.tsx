'use client';

import type { AddressPayload } from '@/types/client';
import { FormEvent, useState } from 'react';
import { Button } from '@/components/ui/Button';

interface AddressFormProps {
  initial?: Partial<AddressPayload>;
  submitLabel?: string;
  onSubmit: (data: AddressPayload) => Promise<void>;
  onCancel?: () => void;
}

export function AddressForm({ initial, submitLabel = 'Enregistrer', onSubmit, onCancel }: AddressFormProps) {
  const [form, setForm] = useState<AddressPayload>({
    label: initial?.label ?? 'Domicile',
    street: initial?.street ?? '',
    city: initial?.city ?? '',
    postalCode: initial?.postalCode ?? '',
    country: initial?.country ?? 'France',
    defaultAddress: initial?.defaultAddress ?? false,
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      await onSubmit(form);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erreur');
    } finally {
      setLoading(false);
    }
  }

  const fields = [
    { key: 'label' as const, label: 'Libellé' },
    { key: 'street' as const, label: 'Rue' },
    { key: 'city' as const, label: 'Ville' },
    { key: 'postalCode' as const, label: 'Code postal' },
    { key: 'country' as const, label: 'Pays' },
  ];

  return (
    <form onSubmit={handleSubmit} className="space-y-4 rounded-2xl border border-slate-200 bg-white p-6">
      {fields.map((field) => (
        <div key={field.key}>
          <label className="block text-sm font-medium">{field.label}</label>
          <input
            required
            value={form[field.key]}
            onChange={(e) => setForm({ ...form, [field.key]: e.target.value })}
            className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm"
          />
        </div>
      ))}
      <label className="flex items-center gap-2 text-sm">
        <input
          type="checkbox"
          checked={form.defaultAddress}
          onChange={(e) => setForm({ ...form, defaultAddress: e.target.checked })}
        />
        Adresse par défaut
      </label>
      {error && <p className="text-sm text-red-600">{error}</p>}
      <div className="flex gap-2">
        <Button type="submit" disabled={loading}>
          {loading ? 'Enregistrement...' : submitLabel}
        </Button>
        {onCancel && (
          <Button type="button" variant="outline" onClick={onCancel}>
            Annuler
          </Button>
        )}
      </div>
    </form>
  );
}
