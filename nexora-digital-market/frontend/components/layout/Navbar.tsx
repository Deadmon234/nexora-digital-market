import Link from 'next/link';

export function Navbar() {
  return (
    <header className="border-b border-slate-200 bg-white">
      <div className="mx-auto flex max-w-6xl items-center justify-between gap-4 px-4 py-4">
        <Link href="/" className="text-lg font-bold text-slate-900">
          Nexora
        </Link>
        <nav className="flex flex-wrap items-center gap-4 text-sm text-slate-700">
          <Link href="/products" className="hover:text-slate-900">
            Catalogue
          </Link>
          <Link href="/categories" className="hover:text-slate-900">
            Catégories
          </Link>
          <Link href="/search" className="hover:text-slate-900">
            Recherche
          </Link>
          <Link href="/cart" className="hover:text-slate-900">
            Panier
          </Link>
          <Link href="/account" className="hover:text-slate-900">
            Mon compte
          </Link>
          <Link href="/seller" className="hover:text-slate-900">
            Espace vendeur
          </Link>
          <Link href="/admin" className="hover:text-slate-900">
            Administration
          </Link>
          <Link href="/auth/login" className="hover:text-slate-900">
            Connexion
          </Link>
        </nav>
      </div>
    </header>
  );
}
