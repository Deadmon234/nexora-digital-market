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
    <header className="sticky top-0 z-50 border-b border-slate-200/80 bg-white/95 backdrop-blur">
      <div className="mx-auto flex max-w-6xl items-center justify-between gap-4 px-4 py-3">
        <Logo size="sm" />
        <nav className="flex flex-wrap items-center justify-end gap-1 text-sm sm:gap-3">
          {navLinks.map((link) => (
            <Link
              key={link.href}
              href={link.href}
              className="rounded-lg px-2 py-1.5 font-medium text-slate-700 transition-colors hover:bg-nexora-blue/5 hover:text-nexora-blue"
            >
              {link.label}
            </Link>
          ))}
          <NavbarActions />
        </nav>
      </div>
    </header>
  );
}
