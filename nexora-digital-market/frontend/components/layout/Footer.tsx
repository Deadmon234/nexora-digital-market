import { Logo } from '@/components/brand/Logo';
import Link from 'next/link';

export function Footer() {
  return (
    <footer className="mt-20 bg-nexora-navy text-white">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div className="grid gap-10 border-b border-white/10 py-12 lg:grid-cols-[1.4fr_1fr_1fr_1.2fr] lg:gap-16">
          <div>
            <div className="inline-flex rounded-xl bg-white px-3 py-2">
              <Logo size="sm" />
            </div>
            <p className="mt-5 max-w-sm text-sm leading-6 text-slate-300">
              Construisons votre avenir numérique. Une marketplace fiable pour choisir, comparer et avancer.
            </p>
            <div className="mt-6 flex items-center gap-2 text-xs font-bold uppercase tracking-[0.18em] text-nexora-green-bright">
              <span className="h-2 w-2 rounded-full bg-nexora-green-bright" />
              Le numérique, en confiance
            </div>
          </div>
          <div>
            <h3 className="text-xs font-bold uppercase tracking-[0.2em] text-nexora-green-bright">Boutique</h3>
            <ul className="mt-5 space-y-3 text-sm text-slate-300">
            <li>
              <Link href="/products" className="transition-colors hover:text-white">
                Catalogue
              </Link>
            </li>
            <li>
              <Link href="/categories" className="transition-colors hover:text-white">
                Catégories
              </Link>
            </li>
            <li>
              <Link href="/search" className="transition-colors hover:text-white">
                Recherche
              </Link>
            </li>
          </ul>
          </div>
          <div>
            <h3 className="text-xs font-bold uppercase tracking-[0.2em] text-nexora-green-bright">Compte</h3>
            <ul className="mt-5 space-y-3 text-sm text-slate-300">
            <li>
              <Link href="/account" className="transition-colors hover:text-white">
                Mon compte
              </Link>
            </li>
            <li>
              <Link href="/seller" className="transition-colors hover:text-white">
                Espace vendeur
              </Link>
            </li>
            <li>
              <Link href="/auth/login" className="transition-colors hover:text-white">
                Connexion
              </Link>
            </li>
          </ul>
          </div>
          <div>
            <h3 className="text-xs font-bold uppercase tracking-[0.2em] text-nexora-green-bright">Besoin d&apos;aide ?</h3>
            <p className="mt-5 text-sm leading-6 text-slate-300">Notre équipe vous accompagne avant et après votre achat.</p>
            <Link href="/account" className="mt-5 inline-flex items-center rounded-full bg-nexora-green px-4 py-2.5 text-sm font-bold text-nexora-navy transition-colors hover:bg-nexora-green-bright">
              Contacter Nexora <span className="ml-2">↗</span>
            </Link>
          </div>
        </div>
        <div className="flex flex-col gap-3 py-5 text-xs text-slate-400 sm:flex-row sm:items-center sm:justify-between">
          <span>© {new Date().getFullYear()} Nexora Digital. Tous droits réservés.</span>
          <div className="flex gap-5">
            <Link href="/products" className="transition-colors hover:text-white">Paiement sécurisé</Link>
            <Link href="/account" className="transition-colors hover:text-white">Confidentialité</Link>
          </div>
        </div>
      </div>
    </footer>
  );
}
