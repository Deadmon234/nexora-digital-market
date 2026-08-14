'use client';

import type { PaymentMethod } from '@/types/payment';
import { FormEvent, useState } from 'react';
import { Button } from '@/components/ui/Button';

interface PaymentFormProps {
  orderId: number;
  amount: number;
  onSubmit: (data: { method: PaymentMethod; cardNumber?: string }) => Promise<void>;
}

export function PaymentForm({ orderId, amount, onSubmit }: PaymentFormProps) {
  const [method, setMethod] = useState<PaymentMethod>('CARD');
  const [cardNumber, setCardNumber] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      await onSubmit({ method, cardNumber: method === 'CARD' ? cardNumber : undefined });
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erreur de paiement');
      setLoading(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4 rounded-2xl border border-slate-200 bg-white p-6">
      <p className="text-sm text-slate-600">
        Commande #{orderId} — <span className="font-bold text-slate-900">{amount.toFixed(2)} €</span>
      </p>

      <div>
        <label className="block text-sm font-medium">Méthode de paiement</label>
        <select
          value={method}
          onChange={(e) => setMethod(e.target.value as PaymentMethod)}
          className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm"
        >
          <option value="CARD">Carte bancaire</option>
          <option value="PAYPAL">PayPal</option>
          <option value="BANK_TRANSFER">Virement</option>
        </select>
      </div>

      {method === 'CARD' && (
        <div>
          <label className="block text-sm font-medium">Numéro de carte</label>
          <input
            required
            placeholder="4242 4242 4242 4242"
            value={cardNumber}
            onChange={(e) => setCardNumber(e.target.value)}
            className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm"
          />
          <p className="mt-1 text-xs text-slate-500">Simulation — tout numéro valide (13+ chiffres) est accepté.</p>
        </div>
      )}

      {error && <p className="text-sm text-red-600">{error}</p>}

      <Button type="submit" disabled={loading} className="w-full">
        {loading ? 'Traitement...' : 'Payer'}
      </Button>
    </form>
  );
}
