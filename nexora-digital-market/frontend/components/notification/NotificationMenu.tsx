'use client';

import { NotificationBell } from '@/components/notification/NotificationBell';
import { NotificationDropdown } from '@/components/notification/NotificationDropdown';
import { useAuth } from '@/components/providers/AuthProvider';
import {
  getNotifications,
  getUnreadCount,
  markAllNotificationsAsRead,
  markNotificationAsRead,
} from '@/services/notification.service';
import type { Notification } from '@/types/notification';
import { useCallback, useEffect, useRef, useState } from 'react';

export function NotificationMenu() {
  const { isAuthenticated } = useAuth();
  const [open, setOpen] = useState(false);
  const [unreadCount, setUnreadCount] = useState(0);
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const containerRef = useRef<HTMLDivElement>(null);

  const refresh = useCallback(async () => {
    if (!isAuthenticated) {
      setUnreadCount(0);
      setNotifications([]);
      return;
    }
    try {
      const [count, list] = await Promise.all([getUnreadCount(), getNotifications()]);
      setUnreadCount(count.count);
      setNotifications(list);
    } catch {
      setUnreadCount(0);
    }
  }, [isAuthenticated]);

  useEffect(() => {
    refresh();
    const interval = setInterval(refresh, 60000);
    return () => clearInterval(interval);
  }, [refresh]);

  useEffect(() => {
    function handleClickOutside(e: MouseEvent) {
      if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
        setOpen(false);
      }
    }
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  if (!isAuthenticated) {
    return null;
  }

  async function handleMarkRead(id: number) {
    await markNotificationAsRead(id);
    refresh();
  }

  async function handleMarkAllRead() {
    await markAllNotificationsAsRead();
    refresh();
  }

  return (
    <div ref={containerRef} className="relative">
      <NotificationBell
        unreadCount={unreadCount}
        onClick={() => {
          setOpen((v) => !v);
          if (!open) refresh();
        }}
      />
      {open && (
        <NotificationDropdown
          notifications={notifications}
          onMarkRead={handleMarkRead}
          onMarkAllRead={handleMarkAllRead}
          onClose={() => setOpen(false)}
        />
      )}
    </div>
  );
}
