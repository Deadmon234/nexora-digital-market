import { Logo } from '@/components/brand/Logo';
import Link from 'next/link';

export function Footer() {
  return (
    <footer className="mt-auto border-t border-slate-200 bg-white">
      <div className="mx-auto grid max-w-6xl gap-8 px-4 py-10 sm:grid-cols-2 lg:grid-cols-4">
        <div className="sm:col-span-2">
          <Logo size="sm" />
          <p className="mt-3 max-w-sm text-sm text-slate-600">
            Construisons votre avenir numérique. Marketplace e-commerce multi-vendeurs pour
            l&apos;électronique et le numérique.
          </p>
        </div>
        <div>
          <h3 className="text-sm font-semibold text-nexora-navy">Boutique</h3>
          <ul className="mt-3 space-y-2 text-sm text-slate-600">
            <li>
              <Link href="/products" className="hover:text-nexora-blue">
                Catalogue
              </Link>
            </li>
            <li>
              <Link href="/categories" className="hover:text-nexora-blue">
                Catégories
              </Link>
            </li>
            <li>
              <Link href="/search" className="hover:text-nexora-blue">
                Recherche
              </Link>
            </li>
          </ul>
        </div>
        <div>
          <h3 className="text-sm font-semibold text-nexora-navy">Compte</h3>
          <ul className="mt-3 space-y-2 text-sm text-slate-600">
            <li>
              <Link href="/account" className="hover:text-nexora-blue">
                Mon compte
              </Link>
            </li>
            <li>
              <Link href="/seller" className="hover:text-nexora-blue">
                Espace vendeur
              </Link>
            </li>
            <li>
              <Link href="/auth/login" className="hover:text-nexora-blue">
                Connexion
              </Link>
            </li>
          </ul>
        </div>
      </div>
      <div className="border-t border-slate-100 bg-slate-50 py-4 text-center text-xs text-slate-500">
        © {new Date().getFullYear()} Nexora Digital. Tous droits réservés.
      </div>
    </footer>
  );
}
