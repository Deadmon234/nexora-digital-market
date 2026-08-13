export default function Home() {
  return (
    <main className="min-h-screen bg-gray-50 text-gray-900">
      <div className="mx-auto max-w-5xl px-6 py-20">
        <h1 className="text-4xl font-bold text-slate-900">Nexora Digital Market</h1>
        <p className="mt-6 max-w-2xl text-lg text-slate-700">
          Plateforme e-commerce multi-vendeurs, gestion de boutiques et produits, tableau de bord administrateur.
        </p>
        <div className="mt-10 grid gap-6 sm:grid-cols-2">
          <article className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm">
            <h2 className="text-xl font-semibold">Vendeur</h2>
            <p className="mt-3 text-slate-600">Créez et gérez votre boutique, ajoutez des produits, suivez le stock.</p>
          </article>
          <article className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm">
            <h2 className="text-xl font-semibold">Admin</h2>
            <p className="mt-3 text-slate-600">Contrôlez toutes les boutiques, produits et vendeurs depuis votre administration.</p>
          </article>
        </div>
      </div>
    </main>
  );
}
