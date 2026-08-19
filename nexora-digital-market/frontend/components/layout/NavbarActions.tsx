'use client';

import Link from 'next/link';
import { NotificationMenu } from '@/components/notification/NotificationMenu';

export function NavbarActions() {
  return (
    <div className="flex items-center gap-2">
      <NotificationMenu />
      <Link
        href="/auth/login"
        className="rounded-full bg-nexora-navy px-4 py-2 text-sm font-bold text-white transition-colors hover:bg-nexora-blue"
      >
        Se connecter
      </Link>
    </div>
  );
}
