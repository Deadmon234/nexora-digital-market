import Link from 'next/link';
import { Card } from '../components/ui/Card';

export default function Home() {
  return (
    <main className="min-h-screen bg-slate-50 text-slate-900">
      <div className="mx-auto max-w-5xl px-6 py-20">
        <h1 className="text-4xl font-bold">Nexora Digital Market</h1>
        <p className="mt-6 max-w-2xl text-lg text-slate-700">
          Marketplace multi-vendeurs de produits electroniques : plusieurs boutiques independantes,
          un seul panier, une comparaison directe des offres.
        </p>
        <div className="mt-8 flex gap-4">
          <Link href="/login" className="rounded-xl bg-slate-900 px-5 py-2.5 text-sm font-medium text-white">
            Se connecter
          </Link>
          <Link
            href="/register"
            className="rounded-xl border border-slate-300 bg-white px-5 py-2.5 text-sm font-medium"
          >
            Creer un compte
          </Link>
        </div>
        <div className="mt-12 grid gap-6 sm:grid-cols-3">
          <Card title="Client">
            <p className="text-slate-600">Comparez les offres de plusieurs vendeurs et suivez vos commandes.</p>
          </Card>
          <Card title="Vendeur">
            <p className="text-slate-600">Ouvrez votre boutique, publiez vos produits et gerez votre stock.</p>
          </Card>
          <Card title="Administrateur">
            <p className="text-slate-600">Pilotez les vendeurs, les boutiques, les commissions et les retraits.</p>
          </Card>
        </div>
      </div>
    </main>
  );
}
