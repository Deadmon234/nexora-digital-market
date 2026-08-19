'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';

const links = [
  { href: '/account', label: 'Aperçu' },
  { href: '/account/profile', label: 'Profil' },
  { href: '/account/addresses', label: 'Adresses' },
  { href: '/account/favorites', label: 'Favoris' },
  { href: '/account/orders', label: 'Commandes' },
  { href: '/account/notifications', label: 'Notifications' },
];

export function AccountSidebar() {
  const pathname = usePathname();

  return (
    <aside className="w-full shrink-0 lg:w-56">
      <nav className="space-y-1 rounded-2xl border border-slate-200 bg-white p-3">
        {links.map((link) => {
          const active = pathname === link.href;
          return (
            <Link
              key={link.href}
              href={link.href}
              className={`block rounded-lg px-3 py-2 text-sm font-medium ${
                active ? 'bg-nexora-blue text-white' : 'text-slate-700 hover:bg-nexora-blue/5 hover:text-nexora-blue'
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
