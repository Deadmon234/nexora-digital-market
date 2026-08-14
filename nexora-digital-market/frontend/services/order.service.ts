import { apiRequest } from '@/services/api.service';
import type { OrderDetail, OrderStatus, OrderSummary, SellerOrder } from '@/types/order';

export function createOrder(addressId: number) {
  return apiRequest<OrderDetail>('/api/orders', { method: 'POST', body: { addressId } });
}

export function getOrders() {
  return apiRequest<OrderSummary[]>('/api/orders');
}

export function getOrder(orderId: number) {
  return apiRequest<OrderDetail>(`/api/orders/${orderId}`);
}

export function getSellerOrders() {
  return apiRequest<SellerOrder[]>('/api/sellers/me/orders');
}

export function getSellerOrder(sellerOrderId: number) {
  return apiRequest<SellerOrder>(`/api/sellers/me/orders/${sellerOrderId}`);
}

export function updateSellerOrderStatus(sellerOrderId: number, status: OrderStatus) {
  return apiRequest<SellerOrder>(`/api/sellers/me/orders/${sellerOrderId}/status`, {
    method: 'PATCH',
    body: { status },
  });
}
