import type { SellerDashboard } from '@/types/seller';

interface DashboardMetricsProps {
  data: SellerDashboard;
}

export function DashboardMetrics({ data }: DashboardMetricsProps) {
  const cards = [
    { label: 'Produits actifs', value: data.totalProducts },
    { label: 'Stock total', value: data.totalStock },
    { label: 'Stock faible', value: data.lowStockProducts },
    { label: 'Statut', value: data.sellerStatus },
  ];

  return (
    <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
      {cards.map((card) => (
        <div key={card.label} className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
          <p className="text-sm text-slate-500">{card.label}</p>
          <p className="mt-2 text-2xl font-bold text-slate-900">{card.value}</p>
        </div>
      ))}
    </div>
  );
}
