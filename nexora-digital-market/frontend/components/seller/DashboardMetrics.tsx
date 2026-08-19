import type { SellerDashboard } from '@/types/seller';

interface DashboardMetricsProps {
  data: SellerDashboard;
}

export function DashboardMetrics({ data }: DashboardMetricsProps) {
  const cards = [
    { label: 'Produits actifs', value: data.totalProducts, tone: 'blue' },
    { label: 'Stock total', value: data.totalStock, tone: 'green' },
    { label: 'Stock faible', value: data.lowStockProducts, tone: 'amber' },
    { label: 'Statut vendeur', value: data.sellerStatus, tone: 'navy' },
  ];

  return (
    <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
      {cards.map((card) => (
        <div key={card.label} className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
          <div className={`h-2 w-10 rounded-full ${card.tone === 'blue' ? 'bg-nexora-blue' : card.tone === 'green' ? 'bg-nexora-green' : card.tone === 'amber' ? 'bg-amber-400' : 'bg-nexora-navy'}`} />
          <p className="mt-4 text-xs font-bold uppercase tracking-[0.12em] text-slate-500">{card.label}</p>
          <p className="mt-2 text-3xl font-bold text-nexora-navy">{card.value}</p>
        </div>
      ))}
    </div>
  );
}
