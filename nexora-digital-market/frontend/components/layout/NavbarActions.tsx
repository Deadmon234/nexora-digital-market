'use client';

import Link from 'next/link';
import { NotificationMenu } from '@/components/notification/NotificationMenu';

export function NavbarActions() {
  return (
    <div className="flex items-center gap-2">
      <NotificationMenu />
      <Link
        href="/auth/login"
        className="rounded-lg bg-nexora-blue px-3 py-1.5 text-sm font-medium text-white transition-colors hover:bg-nexora-blue-dark"
      >
        Connexion
      </Link>
    </div>
  );
}
