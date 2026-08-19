'use client';

import Link from 'next/link';
import type { Notification } from '@/types/notification';

interface NotificationDropdownProps {
  notifications: Notification[];
  onMarkRead: (id: number) => void;
  onMarkAllRead: () => void;
  onClose: () => void;
}

export function NotificationDropdown({
  notifications,
  onMarkRead,
  onMarkAllRead,
  onClose,
}: NotificationDropdownProps) {
  const recent = notifications.slice(0, 5);

  return (
    <div className="absolute right-0 top-full z-50 mt-2 w-80 rounded-xl border border-slate-200 bg-white shadow-lg">
      <div className="flex items-center justify-between border-b border-slate-100 px-4 py-3">
        <h3 className="text-sm font-semibold text-slate-900">Notifications</h3>
        {notifications.some((n) => !n.read) && (
          <button
            type="button"
            onClick={onMarkAllRead}
            className="text-xs text-indigo-600 hover:underline"
          >
            Tout lire
          </button>
        )}
      </div>
      <div className="max-h-80 overflow-y-auto">
        {recent.length === 0 ? (
          <p className="px-4 py-6 text-center text-sm text-slate-500">Aucune notification</p>
        ) : (
          recent.map((n) => (
            <div
              key={n.id}
              className={`border-b border-slate-50 px-4 py-3 last:border-0 ${
                n.read ? 'bg-white' : 'bg-indigo-50/50'
              }`}
            >
              <div className="flex items-start justify-between gap-2">
                <div className="min-w-0 flex-1">
                  <p className="text-sm font-medium text-slate-900">{n.title}</p>
                  <p className="mt-0.5 line-clamp-2 text-xs text-slate-600">{n.message}</p>
                  <p className="mt-1 text-[10px] text-slate-400">
                    {new Date(n.createdAt).toLocaleString('fr-FR')}
                  </p>
                </div>
                {!n.read && (
                  <button
                    type="button"
                    onClick={() => onMarkRead(n.id)}
                    className="shrink-0 text-[10px] text-indigo-600 hover:underline"
                  >
                    Lu
                  </button>
                )}
              </div>
              {n.linkUrl && (
                <Link
                  href={n.linkUrl}
                  onClick={onClose}
                  className="mt-1 inline-block text-xs text-indigo-600 hover:underline"
                >
                  Voir détails
                </Link>
              )}
            </div>
          ))
        )}
      </div>
      <div className="border-t border-slate-100 px-4 py-2">
        <Link
          href="/account/notifications"
          onClick={onClose}
          className="block text-center text-xs font-medium text-indigo-600 hover:underline"
        >
          Voir tout
        </Link>
      </div>
    </div>
  );
}
