'use client';

import { Button } from '@/components/ui/Button';
import {
  deleteNotification,
  getNotifications,
  markAllNotificationsAsRead,
  markNotificationAsRead,
} from '@/services/notification.service';
import type { Notification } from '@/types/notification';
import Link from 'next/link';
import { useCallback, useEffect, useState } from 'react';

export function NotificationCenter() {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [loading, setLoading] = useState(true);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      setNotifications(await getNotifications());
    } catch {
      setNotifications([]);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  async function handleMarkRead(id: number) {
    await markNotificationAsRead(id);
    load();
  }

  async function handleMarkAllRead() {
    await markAllNotificationsAsRead();
    load();
  }

  async function handleDelete(id: number) {
    await deleteNotification(id);
    load();
  }

  if (loading) {
    return <p className="text-slate-600">Chargement...</p>;
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Notifications</h1>
          <p className="mt-1 text-sm text-slate-600">
            {notifications.filter((n) => !n.read).length} non lue(s)
          </p>
        </div>
        {notifications.some((n) => !n.read) && (
          <Button type="button" variant="secondary" onClick={handleMarkAllRead}>
            Tout marquer comme lu
          </Button>
        )}
      </div>

      {notifications.length === 0 ? (
        <p className="rounded-xl border border-slate-200 bg-white p-8 text-center text-sm text-slate-500">
          Aucune notification pour le moment.
        </p>
      ) : (
        <div className="space-y-3">
          {notifications.map((n) => (
            <article
              key={n.id}
              className={`rounded-xl border p-4 ${
                n.read ? 'border-slate-200 bg-white' : 'border-indigo-200 bg-indigo-50/40'
              }`}
            >
              <div className="flex items-start justify-between gap-4">
                <div>
                  <p className="font-medium text-slate-900">{n.title}</p>
                  <p className="mt-1 text-sm text-slate-700">{n.message}</p>
                  <p className="mt-2 text-xs text-slate-400">
                    {new Date(n.createdAt).toLocaleString('fr-FR')}
                  </p>
                  {n.linkUrl && (
                    <Link href={n.linkUrl} className="mt-2 inline-block text-sm text-indigo-600 hover:underline">
                      Voir détails →
                    </Link>
                  )}
                </div>
                <div className="flex shrink-0 flex-col gap-2">
                  {!n.read && (
                    <button
                      type="button"
                      onClick={() => handleMarkRead(n.id)}
                      className="text-xs text-indigo-600 hover:underline"
                    >
                      Marquer lu
                    </button>
                  )}
                  <button
                    type="button"
                    onClick={() => handleDelete(n.id)}
                    className="text-xs text-red-600 hover:underline"
                  >
                    Supprimer
                  </button>
                </div>
              </div>
            </article>
          ))}
        </div>
      )}
    </div>
  );
}
