import { formatPrice } from '@/services/catalog.service';
import type { SellerBalance } from '@/types/payment';

interface BalanceSummaryProps {
  balance: SellerBalance;
}

export function BalanceSummary({ balance }: BalanceSummaryProps) {
  const cards = [
    { label: 'Solde disponible', value: balance.availableBalance },
    { label: 'Total gagné', value: balance.totalEarned },
    { label: 'Total retiré', value: balance.totalWithdrawn },
  ];

  return (
    <div className="grid gap-4 sm:grid-cols-3">
      {cards.map((card) => (
        <div key={card.label} className="rounded-2xl border border-slate-200 bg-white p-5">
          <p className="text-sm text-slate-500">{card.label}</p>
          <p className="mt-2 text-2xl font-bold text-slate-900">{formatPrice(card.value)}</p>
        </div>
      ))}
    </div>
  );
}
