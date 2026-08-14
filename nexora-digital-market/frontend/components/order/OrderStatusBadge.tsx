import type { OrderStatus } from '@/types/order';
import { ORDER_STATUS_LABELS } from '@/types/order';

const statusStyles: Record<OrderStatus, string> = {
  PENDING: 'bg-slate-100 text-slate-700',
  CONFIRMED: 'bg-blue-100 text-blue-800',
  PROCESSING: 'bg-amber-100 text-amber-800',
  SHIPPED: 'bg-indigo-100 text-indigo-800',
  DELIVERED: 'bg-green-100 text-green-800',
  CANCELLED: 'bg-red-100 text-red-800',
};

interface OrderStatusBadgeProps {
  status: OrderStatus;
}

export function OrderStatusBadge({ status }: OrderStatusBadgeProps) {
  return (
    <span className={`inline-flex rounded-full px-2.5 py-0.5 text-xs font-medium ${statusStyles[status]}`}>
      {ORDER_STATUS_LABELS[status]}
    </span>
  );
}
