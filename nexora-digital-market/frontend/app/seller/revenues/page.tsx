'use client';

import { BalanceSummary } from '@/components/payment/BalanceSummary';
import { formatPrice } from '@/services/catalog.service';
import { getSellerBalance, getSellerCommissions } from '@/services/payment.service';
import type { Commission, SellerBalance } from '@/types/payment';
import { useEffect, useState } from 'react';

export default function SellerRevenuesPage() {
  const [balance, setBalance] = useState<SellerBalance | null>(null);
  const [commissions, setCommissions] = useState<Commission[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([getSellerBalance(), getSellerCommissions()])
      .then(([b, c]) => {
        setBalance(b);
        setCommissions(c);
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return <p className="text-slate-600">Chargement...</p>;
  }

  if (!balance) {
    return <p className="text-slate-600">Impossible de charger les revenus.</p>;
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">Mes revenus</h1>
      <BalanceSummary balance={balance} />

      <div>
        <h2 className="text-lg font-semibold text-slate-900">Historique des commissions</h2>
        {commissions.length === 0 ? (
          <p className="mt-2 text-sm text-slate-600">Aucune commission pour le moment.</p>
        ) : (
          <ul className="mt-4 space-y-2">
            {commissions.map((c) => (
              <li
                key={c.id}
                className="flex flex-wrap items-center justify-between gap-2 rounded-xl border border-slate-200 bg-white px-4 py-3 text-sm"
              >
                <span>{c.orderNumber}</span>
                <span className="text-slate-500">
                  Commission {c.commissionRate}% : {formatPrice(c.commissionAmount)}
                </span>
                <span className="font-semibold text-green-700">+{formatPrice(c.sellerAmount)}</span>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
