import type { OrderStatus } from '@/types/order';
import { ORDER_STATUS_LABELS } from '@/types/order';
import { OrderStatusBadge } from '@/components/order/OrderStatusBadge';

const FLOW: OrderStatus[] = ['CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED'];

interface OrderTimelineProps {
  status: OrderStatus;
}

export function OrderTimeline({ status }: OrderTimelineProps) {
  if (status === 'CANCELLED') {
    return (
      <div className="rounded-xl border border-red-200 bg-red-50 p-4 text-sm text-red-800">
        Commande annulée
      </div>
    );
  }

  const currentIndex = FLOW.indexOf(status);

  return (
    <ol className="space-y-3">
      {FLOW.map((step, index) => {
        const done = currentIndex >= index;
        return (
          <li key={step} className="flex items-center gap-3">
            <span
              className={`flex h-8 w-8 items-center justify-center rounded-full text-xs font-bold ${
                done ? 'bg-slate-900 text-white' : 'bg-slate-200 text-slate-500'
              }`}
            >
              {index + 1}
            </span>
            <div>
              <p className={`text-sm font-medium ${done ? 'text-slate-900' : 'text-slate-400'}`}>
                {ORDER_STATUS_LABELS[step]}
              </p>
            </div>
            {index === currentIndex && <OrderStatusBadge status={status} />}
          </li>
        );
      })}
    </ol>
  );
}
