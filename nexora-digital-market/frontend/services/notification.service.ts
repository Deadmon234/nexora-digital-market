import { apiRequest } from '@/services/api.service';
import type { Notification, UnreadCount } from '@/types/notification';

export function getNotifications() {
  return apiRequest<Notification[]>('/api/notifications');
}

export function getUnreadCount() {
  return apiRequest<UnreadCount>('/api/notifications/unread-count');
}

export function markNotificationAsRead(id: number) {
  return apiRequest<Notification>(`/api/notifications/${id}/read`, { method: 'PATCH' });
}

export function markAllNotificationsAsRead() {
  return apiRequest<void>('/api/notifications/read-all', { method: 'PATCH' });
}

export function deleteNotification(id: number) {
  return apiRequest<void>(`/api/notifications/${id}`, { method: 'DELETE' });
}
