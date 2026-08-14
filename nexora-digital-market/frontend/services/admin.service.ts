import { apiRequest } from '@/services/api.service';
import type {
  AdminAnalytics,
  AdminBrand,
  AdminCategory,
  AdminCommission,
  AdminOrderSummary,
  AdminProduct,
  AdminSeller,
  AdminShop,
  AdminWithdrawal,
  SellerStatus,
  ShopStatus,
  WithdrawalStatus,
} from '@/types/admin';

export function getAdminAnalytics() {
  return apiRequest<AdminAnalytics>('/api/admin/analytics');
}

export function getAdminSellers() {
  return apiRequest<AdminSeller[]>('/api/admin/sellers');
}

export function updateSellerStatus(id: number, status: SellerStatus) {
  return apiRequest<AdminSeller>(`/api/admin/sellers/${id}/status`, {
    method: 'PATCH',
    body: { status },
  });
}

export function updateSellerCommissionRate(id: number, commissionRate: number) {
  return apiRequest<AdminSeller>(`/api/admin/sellers/${id}/commission-rate`, {
    method: 'PATCH',
    body: { commissionRate },
  });
}

export function getAdminShops() {
  return apiRequest<AdminShop[]>('/api/admin/shops');
}

export function updateShopStatus(id: number, status: ShopStatus) {
  return apiRequest<AdminShop>(`/api/admin/shops/${id}/status`, {
    method: 'PATCH',
    body: { status },
  });
}

export function updateShopActive(id: number, active: boolean) {
  return apiRequest<AdminShop>(`/api/admin/shops/${id}/active`, {
    method: 'PATCH',
    body: { active },
  });
}

export function getAdminProducts() {
  return apiRequest<AdminProduct[]>('/api/admin/products');
}

export function updateProductActive(id: number, active: boolean) {
  return apiRequest<AdminProduct>(`/api/admin/products/${id}/active`, {
    method: 'PATCH',
    body: { active },
  });
}

export function getAdminOrders() {
  return apiRequest<AdminOrderSummary[]>('/api/admin/orders');
}

export function getAdminCategories() {
  return apiRequest<AdminCategory[]>('/api/admin/categories');
}

export function createCategory(data: { name: string; slug?: string; description?: string }) {
  return apiRequest<AdminCategory>('/api/admin/categories', { method: 'POST', body: data });
}

export function getAdminBrands() {
  return apiRequest<AdminBrand[]>('/api/admin/brands');
}

export function createBrand(data: { name: string; slug?: string; description?: string }) {
  return apiRequest<AdminBrand>('/api/admin/brands', { method: 'POST', body: data });
}

export function getAdminCommissions() {
  return apiRequest<AdminCommission[]>('/api/admin/commissions');
}

export function getAdminWithdrawals() {
  return apiRequest<AdminWithdrawal[]>('/api/admin/withdrawals');
}

export function updateWithdrawalStatus(id: number, status: WithdrawalStatus) {
  return apiRequest<AdminWithdrawal>(`/api/admin/withdrawals/${id}/status`, {
    method: 'PATCH',
    body: { status },
  });
}
