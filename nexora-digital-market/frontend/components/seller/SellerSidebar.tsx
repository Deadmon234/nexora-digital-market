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
    <aside className="w-full shrink-0 lg:w-64">
      <div className="rounded-2xl bg-nexora-navy p-5 text-white shadow-lg">
        <p className="text-[10px] font-bold uppercase tracking-[0.22em] text-nexora-green-bright">Nexora Seller Center</p>
        <h2 className="mt-3 text-xl font-bold">Pilotez votre activité</h2>
        <p className="mt-2 text-xs leading-5 text-slate-300">Gérez votre boutique, vos produits et vos commandes au même endroit.</p>
      </div>
      <nav className="mt-4 rounded-2xl border border-slate-200 bg-white p-3 shadow-sm">
        <p className="px-3 pb-2 text-[10px] font-bold uppercase tracking-[0.2em] text-slate-400">Menu vendeur</p>
        {links.map((link) => {
          const active = pathname === link.href || pathname.startsWith(`${link.href}/`);
          return (
            <Link
              key={link.href}
              href={link.href}
              className={`flex items-center justify-between rounded-xl px-3 py-3 text-sm font-semibold transition-colors ${
                active ? 'bg-nexora-blue text-white shadow-sm' : 'text-slate-600 hover:bg-nexora-blue/5 hover:text-nexora-blue'
              }`}
            >
              <span>{link.label}</span>
              {active && <span aria-hidden="true">→</span>}
            </Link>
          );
        })}
      </nav>
    </aside>
  );
}
