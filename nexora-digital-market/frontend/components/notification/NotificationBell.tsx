'use client';

import Link from 'next/link';

interface NotificationBellProps {
  unreadCount: number;
  onClick?: () => void;
}

export function NotificationBell({ unreadCount, onClick }: NotificationBellProps) {
  return (
    <button
      type="button"
      onClick={onClick}
      className="relative rounded-lg p-2 text-slate-600 hover:bg-slate-100 hover:text-slate-900"
      aria-label={`Notifications${unreadCount > 0 ? ` (${unreadCount} non lues)` : ''}`}
    >
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="h-5 w-5">
        <path d="M5.85 3.5a.75.75 0 00-1.117-1 9.719 9.719 0 00-2.348 4.876.75.75 0 001.06.853A8.219 8.219 0 015.85 3.5zM19.267 2.5a.75.75 0 10-1.118 1 8.22 8.22 0 013.987 4.124.75.75 0 001.06-.853 9.719 9.719 0 00-2.35-4.271zM12 2.25a6.75 6.75 0 00-6.75 6.75v3.27l-.857 1.715A.75.75 0 004.5 15h15a.75.75 0 00.671-1.018l-.857-1.715V9A6.75 6.75 0 0012 2.25zM9 18.75A3 3 0 0012 21a3 3 0 003-2.25H9z" />
      </svg>
      {unreadCount > 0 && (
        <span className="absolute -right-0.5 -top-0.5 flex h-4 min-w-4 items-center justify-center rounded-full bg-red-500 px-1 text-[10px] font-bold text-white">
          {unreadCount > 9 ? '9+' : unreadCount}
        </span>
      )}
    </button>
  );
}

export function NotificationBellLink({ unreadCount }: { unreadCount: number }) {
  return (
    <Link
      href="/account/notifications"
      className="relative inline-flex rounded-lg p-2 text-slate-600 hover:bg-slate-100 hover:text-slate-900"
      aria-label={`Notifications${unreadCount > 0 ? ` (${unreadCount} non lues)` : ''}`}
    >
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="h-5 w-5">
        <path d="M5.85 3.5a.75.75 0 00-1.117-1 9.719 9.719 0 00-2.348 4.876.75.75 0 001.06.853A8.219 8.219 0 015.85 3.5zM19.267 2.5a.75.75 0 10-1.118 1 8.22 8.22 0 013.987 4.124.75.75 0 001.06-.853 9.719 9.719 0 00-2.35-4.271zM12 2.25a6.75 6.75 0 00-6.75 6.75v3.27l-.857 1.715A.75.75 0 004.5 15h15a.75.75 0 00.671-1.018l-.857-1.715V9A6.75 6.75 0 0012 2.25zM9 18.75A3 3 0 0012 21a3 3 0 003-2.25H9z" />
      </svg>
      {unreadCount > 0 && (
        <span className="absolute -right-0.5 -top-0.5 flex h-4 min-w-4 items-center justify-center rounded-full bg-red-500 px-1 text-[10px] font-bold text-white">
          {unreadCount > 9 ? '9+' : unreadCount}
        </span>
      )}
    </Link>
  );
}
