import { Card } from '../../components/ui/Card';
import { SessionSummary } from '../../components/session/SessionSummary';

export default function SellerPage() {
  return (
    <main className="mx-auto max-w-4xl px-6 py-16">
      <h1 className="text-2xl font-bold text-slate-900">Espace vendeur</h1>
      <div className="mt-8 grid gap-6">
        <SessionSummary />
        <Card title="Prochaines etapes">
          <p className="text-slate-600">
            Boutique, produits, stock, commandes et revenus arrivent dans les phases suivantes.
          </p>
        </Card>
      </div>
    </main>
  );
}
