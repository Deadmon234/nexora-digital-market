import { Card } from '../../components/ui/Card';
import { SessionSummary } from '../../components/session/SessionSummary';

export default function AdminPage() {
  return (
    <main className="mx-auto max-w-4xl px-6 py-16">
      <h1 className="text-2xl font-bold text-slate-900">Administration</h1>
      <div className="mt-8 grid gap-6">
        <SessionSummary />
        <Card title="Prochaines etapes">
          <p className="text-slate-600">
            Validation des vendeurs et des boutiques, catalogue, commissions et retraits arrivent
            dans les phases suivantes.
          </p>
        </Card>
      </div>
    </main>
  );
}
