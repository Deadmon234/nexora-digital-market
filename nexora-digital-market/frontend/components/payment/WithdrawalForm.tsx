'use client';

import type { WithdrawalPayload } from '@/types/payment';
import { FormEvent, useState } from 'react';
import { Button } from '@/components/ui/Button';

interface WithdrawalFormProps {
  maxAmount: number;
  onSubmit: (data: WithdrawalPayload) => Promise<void>;
}

export function WithdrawalForm({ maxAmount, onSubmit }: WithdrawalFormProps) {
  const [amount, setAmount] = useState('');
  const [bankAccount, setBankAccount] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      await onSubmit({ amount: Number(amount), bankAccount });
      setAmount('');
      setBankAccount('');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erreur');
    } finally {
      setLoading(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4 rounded-2xl border border-slate-200 bg-white p-6">
      <p className="text-sm text-slate-600">Solde disponible : {maxAmount.toFixed(2)} € (min. 10 €)</p>
      <div>
        <label className="block text-sm font-medium">Montant (€)</label>
        <input
          type="number"
          min="10"
          max={maxAmount}
          step="0.01"
          required
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm"
        />
      </div>
      <div>
        <label className="block text-sm font-medium">Compte bancaire (IBAN)</label>
        <input
          required
          value={bankAccount}
          onChange={(e) => setBankAccount(e.target.value)}
          placeholder="FR76 1234 5678 9012 3456 7890 123"
          className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm"
        />
      </div>
      {error && <p className="text-sm text-red-600">{error}</p>}
      <Button type="submit" disabled={loading || maxAmount < 10}>
        {loading ? 'Envoi...' : 'Demander le retrait'}
      </Button>
    </form>
  );
}
