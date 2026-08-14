'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';

const links = [
  { href: '/admin', label: 'Dashboard' },
  { href: '/admin/sellers', label: 'Vendeurs' },
  { href: '/admin/shops', label: 'Boutiques' },
  { href: '/admin/products', label: 'Produits' },
  { href: '/admin/orders', label: 'Commandes' },
  { href: '/admin/categories', label: 'Catégories' },
  { href: '/admin/brands', label: 'Marques' },
  { href: '/admin/commissions', label: 'Commissions' },
  { href: '/admin/withdrawals', label: 'Retraits' },
  { href: '/admin/statistics', label: 'Statistiques' },
];

export function AdminSidebar() {
  const pathname = usePathname();

  return (
    <aside className="w-full shrink-0 lg:w-56">
      <nav className="space-y-1 rounded-2xl border border-slate-200 bg-white p-3">
        {links.map((link) => {
          const active = pathname === link.href || (link.href !== '/admin' && pathname.startsWith(`${link.href}/`));
          return (
            <Link
              key={link.href}
              href={link.href}
              className={`block rounded-lg px-3 py-2 text-sm font-medium ${
                active ? 'bg-indigo-600 text-white' : 'text-slate-700 hover:bg-slate-100'
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
