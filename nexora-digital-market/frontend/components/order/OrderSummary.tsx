import { formatPrice } from '@/services/catalog.service';
import type { OrderDetail, SellerOrder } from '@/types/order';
import { OrderStatusBadge } from '@/components/order/OrderStatusBadge';

interface OrderSummaryProps {
  order: OrderDetail | SellerOrder;
  showShipping?: boolean;
  shipping?: Pick<OrderDetail, 'shippingLabel' | 'shippingStreet' | 'shippingCity' | 'shippingPostalCode' | 'shippingCountry'>;
}

export function OrderSummaryCard({ order, showShipping, shipping }: OrderSummaryProps) {
  const isFullOrder = 'orderNumber' in order;
  const items = isFullOrder ? order.sellerOrders.flatMap((so) => so.items) : order.items;
  const total = isFullOrder ? order.totalAmount : order.subtotal;

  return (
    <div className="space-y-4 rounded-2xl border border-slate-200 bg-white p-6">
      <div className="flex flex-wrap items-center justify-between gap-2">
        <h2 className="text-lg font-semibold text-slate-900">
          {isFullOrder ? `Commande ${order.orderNumber}` : `Commande vendeur`}
        </h2>
        <OrderStatusBadge status={order.status} />
      </div>

      {showShipping && shipping && (
        <div className="text-sm text-slate-600">
          <p className="font-medium text-slate-900">{shipping.shippingLabel}</p>
          <p>
            {shipping.shippingStreet}, {shipping.shippingPostalCode} {shipping.shippingCity},{' '}
            {shipping.shippingCountry}
          </p>
        </div>
      )}

      <ul className="divide-y divide-slate-100">
        {items.map((item) => (
          <li key={item.id} className="flex justify-between gap-4 py-3 text-sm">
            <div>
              <p className="font-medium text-slate-900">{item.productName}</p>
              <p className="text-slate-500">
                {item.sellerName} · x{item.quantity}
              </p>
            </div>
            <span className="font-medium">{formatPrice(item.lineTotal)}</span>
          </li>
        ))}
      </ul>

      <div className="flex justify-between border-t border-slate-200 pt-4 text-base font-bold">
        <span>Total</span>
        <span>{formatPrice(total)}</span>
      </div>
    </div>
  );
}
