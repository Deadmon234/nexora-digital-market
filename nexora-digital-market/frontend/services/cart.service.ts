import { apiRequest } from '@/services/api.service';
import type { Cart } from '@/types/client';

export function getCart() {
  return apiRequest<Cart>('/api/cart');
}

export function addToCart(offerId: number, quantity = 1) {
  return apiRequest<Cart>('/api/cart/items', { method: 'POST', body: { offerId, quantity } });
}

export function updateCartItem(itemId: number, quantity: number) {
  return apiRequest<Cart>(`/api/cart/items/${itemId}`, { method: 'PUT', body: { quantity } });
}

export function removeCartItem(itemId: number) {
  return apiRequest<Cart>(`/api/cart/items/${itemId}`, { method: 'DELETE' });
}

export function clearCart() {
  return apiRequest<Cart>('/api/cart', { method: 'DELETE' });
}
