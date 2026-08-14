'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';

const links = [
  { href: '/seller', label: 'Dashboard' },
  { href: '/seller/shop', label: 'Boutique' },
  { href: '/seller/products', label: 'Produits' },
  { href: '/seller/inventory', label: 'Stock' },
  { href: '/seller/orders', label: 'Commandes' },
  { href: '/seller/revenues', label: 'Revenus' },
  { href: '/seller/withdrawals', label: 'Retraits' },
];

export function SellerSidebar() {
  const pathname = usePathname();

  return (
    <aside className="w-full shrink-0 lg:w-56">
      <nav className="space-y-1 rounded-2xl border border-slate-200 bg-white p-3">
        {links.map((link) => {
          const active = pathname === link.href || pathname.startsWith(`${link.href}/`);
          return (
            <Link
              key={link.href}
              href={link.href}
              className={`block rounded-lg px-3 py-2 text-sm font-medium ${
                active ? 'bg-slate-900 text-white' : 'text-slate-700 hover:bg-slate-100'
              }`}
            >
              {link.label}
            </Link>
          );
        })}
      </nav>
    </aside>
  );
}
