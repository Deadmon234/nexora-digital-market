export type SellerStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'SUSPENDED';
export type ShopStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'SUSPENDED';
export type WithdrawalStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'PAID';

export interface AdminAnalytics {
  totalUsers: number;
  totalSellers: number;
  pendingSellers: number;
  totalProducts: number;
  totalOrders: number;
  pendingOrders: number;
  totalRevenue: number;
  totalCommissions: number;
  pendingWithdrawals: number;
}

export interface AdminSeller {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  companyName: string;
  taxId?: string;
  status: SellerStatus;
  commissionRate: number;
  createdAt: string;
}

export interface AdminShop {
  id: number;
  sellerId: number;
  sellerCompanyName: string;
  name: string;
  slug: string;
  status: ShopStatus;
  active: boolean;
  createdAt: string;
}

export interface AdminProduct {
  id: number;
  name: string;
  slug: string;
  categoryName?: string;
  brandName?: string;
  offerCount: number;
  active: boolean;
  createdAt: string;
}

export interface AdminCommission {
  id: number;
  sellerId: number;
  sellerCompanyName: string;
  orderNumber: string;
  orderAmount: number;
  commissionRate: number;
  commissionAmount: number;
  sellerAmount: number;
  createdAt: string;
}

export interface AdminWithdrawal {
  id: number;
  sellerId: number;
  sellerCompanyName: string;
  amount: number;
  bankAccount: string;
  status: WithdrawalStatus;
  createdAt: string;
  processedAt?: string;
}

export interface AdminOrderSummary {
  id: number;
  orderNumber: string;
  status: string;
  totalAmount: number;
  itemCount: number;
  sellerCount: number;
  createdAt: string;
}

export interface AdminCategory {
  id: number;
  name: string;
  slug: string;
  description?: string;
  active: boolean;
}

export interface AdminBrand {
  id: number;
  name: string;
  slug: string;
  description?: string;
  logoUrl?: string;
  active: boolean;
}
