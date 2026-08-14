import { apiRequest } from '@/services/api.service';
import type { PageResponse } from '@/types/product';
import type {
  InventoryItem,
  InventoryMovement,
  SellerDashboard,
  SellerProduct,
  SellerProductPayload,
  SellerProfile,
  Shop,
} from '@/types/seller';

export function applyAsSeller(data: { companyName: string; taxId?: string }) {
  return apiRequest<SellerProfile>('/api/sellers/apply', { method: 'POST', body: data });
}

export function getSellerProfile() {
  return apiRequest<SellerProfile>('/api/sellers/me');
}

export function getSellerDashboard() {
  return apiRequest<SellerDashboard>('/api/sellers/me/dashboard');
}

export function getMyShop() {
  return apiRequest<Shop>('/api/sellers/me/shop');
}

export function updateMyShop(data: Partial<Shop>) {
  return apiRequest<Shop>('/api/sellers/me/shop', { method: 'PUT', body: data });
}

export function getSellerProducts() {
  return apiRequest<SellerProduct[]>('/api/sellers/me/products');
}

export function getSellerProduct(offerId: number) {
  return apiRequest<SellerProduct>(`/api/sellers/me/products/${offerId}`);
}

export function createSellerProduct(data: SellerProductPayload) {
  return apiRequest<SellerProduct>('/api/sellers/me/products', { method: 'POST', body: data });
}

export function updateSellerProduct(offerId: number, data: SellerProductPayload) {
  return apiRequest<SellerProduct>(`/api/sellers/me/products/${offerId}`, { method: 'PUT', body: data });
}

export function deleteSellerProduct(offerId: number) {
  return apiRequest<void>(`/api/sellers/me/products/${offerId}`, { method: 'DELETE' });
}

export function getInventory() {
  return apiRequest<InventoryItem[]>('/api/sellers/me/inventory');
}

export function addStock(offerId: number, quantity: number, reason?: string) {
  return apiRequest<InventoryItem>('/api/sellers/me/inventory/add', {
    method: 'POST',
    body: { offerId, quantity, reason },
  });
}

export function decreaseStock(offerId: number, quantity: number, reason?: string) {
  return apiRequest<InventoryItem>('/api/sellers/me/inventory/decrease', {
    method: 'POST',
    body: { offerId, quantity, reason },
  });
}

export function getInventoryHistory(page = 0, size = 20) {
  return apiRequest<PageResponse<InventoryMovement>>(
    `/api/sellers/me/inventory/history?page=${page}&size=${size}`,
  );
}
