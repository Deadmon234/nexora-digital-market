'use client';

import Link from 'next/link';
import { NotificationMenu } from '@/components/notification/NotificationMenu';
import { useAuth } from '@/components/providers/AuthProvider';

export function NavbarActions() {
  const { user, isLoading, isAuthenticated, logout } = useAuth();

  if (isLoading || !isAuthenticated || !user) {
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

  const displayName = [user.firstName, user.lastName].filter(Boolean).join(' ') || user.email;
  const greeting = new Date().getHours() < 18 ? 'Bonjour' : 'Bonsoir';

  return (
    <div className="flex items-center gap-3">
      <NotificationMenu />
      <span className="max-w-[9rem] truncate text-xs font-semibold text-nexora-navy sm:max-w-none sm:text-sm">
        {greeting}, {displayName}
      </span>
      <button
        type="button"
        onClick={() => void logout()}
        className="rounded-full border border-nexora-navy px-4 py-2 text-sm font-bold text-nexora-navy transition-colors hover:bg-nexora-navy hover:text-white"
      >
        Déconnexion
      </button>
    </div>
  );
}
