export type NotificationType =
  | 'ORDER_CREATED'
  | 'PAYMENT_COMPLETED'
  | 'ORDER_STATUS_UPDATED'
  | 'NEW_SELLER_ORDER'
  | 'SYSTEM';

export interface Notification {
  id: number;
  type: NotificationType;
  title: string;
  message: string;
  linkUrl?: string;
  read: boolean;
  createdAt: string;
}

export interface UnreadCount {
  count: number;
}
