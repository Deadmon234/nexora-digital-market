import { Logo } from '@/components/brand/Logo';
import { NavbarActions } from '@/components/layout/NavbarActions';
import Link from 'next/link';

const navLinks = [
  { href: '/products', label: 'Catalogue' },
  { href: '/categories', label: 'Catégories' },
  { href: '/search', label: 'Recherche' },
  { href: '/cart', label: 'Panier' },
  { href: '/account', label: 'Mon compte' },
  { href: '/seller', label: 'Espace vendeur' },
  { href: '/admin', label: 'Administration' },
];

export function Navbar() {
  return (
    <header className="sticky top-0 z-50 border-b border-slate-200/80 bg-nexora-ivory/95 shadow-[0_1px_0_rgba(9,42,92,0.03)] backdrop-blur">
      <div className="mx-auto flex max-w-7xl items-center justify-between gap-4 px-4 py-4 sm:px-6 lg:px-8">
        <Logo size="sm" />
        <nav className="hidden items-center gap-1 text-sm md:flex lg:gap-2">
          {navLinks.map((link) => (
            <Link
              key={link.href}
              href={link.href}
              className="rounded-full px-3 py-2 font-semibold text-slate-600 transition-colors hover:bg-white hover:text-nexora-blue"
            >
              {link.label}
            </Link>
          ))}
          <NavbarActions />
        </nav>
        <div className="flex items-center gap-3 md:hidden">
          <Link href="/products" className="text-sm font-bold text-nexora-blue">Boutique</Link>
          <NavbarActions />
        </div>
      </div>
    </header>
  );
}
