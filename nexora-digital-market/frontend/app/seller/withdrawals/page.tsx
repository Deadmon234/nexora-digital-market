'use client';

import { WithdrawalForm } from '@/components/payment/WithdrawalForm';
import { getSellerBalance, getWithdrawals, requestWithdrawal } from '@/services/payment.service';
import type { SellerBalance, Withdrawal } from '@/types/payment';
import { useEffect, useState } from 'react';

export default function SellerWithdrawalsPage() {
  const [balance, setBalance] = useState<SellerBalance | null>(null);
  const [withdrawals, setWithdrawals] = useState<Withdrawal[]>([]);
  const [loading, setLoading] = useState(true);

  async function load() {
    setLoading(true);
    try {
      const [b, w] = await Promise.all([getSellerBalance(), getWithdrawals()]);
      setBalance(b);
      setWithdrawals(w);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  if (loading || !balance) {
    return <p className="text-slate-600">Chargement...</p>;
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">Retraits</h1>

      <WithdrawalForm
        maxAmount={balance.availableBalance}
        onSubmit={async (data) => {
          await requestWithdrawal(data);
          await load();
        }}
      />

      {withdrawals.length > 0 && (
        <div>
          <h2 className="text-lg font-semibold text-slate-900">Historique</h2>
          <ul className="mt-4 space-y-2">
            {withdrawals.map((w) => (
              <li
                key={w.id}
                className="flex justify-between rounded-xl border border-slate-200 bg-white px-4 py-3 text-sm"
              >
                <span>{w.amount.toFixed(2)} € → {w.bankAccount}</span>
                <span className="font-medium text-slate-600">{w.status}</span>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
