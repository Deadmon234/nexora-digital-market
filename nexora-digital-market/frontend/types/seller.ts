export type SellerStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'SUSPENDED';
export type ShopStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'SUSPENDED';

export interface SellerProfile {
  id: number;
  email: string;
  firstName?: string;
  lastName?: string;
  companyName?: string;
  taxId?: string;
  status: SellerStatus;
  commissionRate?: number;
}

export interface SellerDashboard {
  totalProducts: number;
  totalStock: number;
  lowStockProducts: number;
  shopName?: string;
  sellerStatus: string;
}

export interface Shop {
  id: number;
  name: string;
  slug: string;
  description?: string;
  logoUrl?: string;
  bannerUrl?: string;
  status: ShopStatus;
  active: boolean;
}

export interface SellerProduct {
  offerId: number;
  productId: number;
  name: string;
  slug: string;
  description?: string;
  imageUrl?: string;
  price: number;
  stock: number;
  conditionLabel?: string;
  active: boolean;
}

export interface InventoryItem {
  offerId: number;
  productId: number;
  productName: string;
  stock: number;
  lowStock: boolean;
}

export interface InventoryMovement {
  id: number;
  offerId: number;
  productName: string;
  type: 'ADD' | 'DECREASE' | 'ADJUSTMENT';
  quantity: number;
  stockBefore: number;
  stockAfter: number;
  reason?: string;
  createdAt: string;
}

export interface SellerProductPayload {
  name: string;
  description?: string;
  categoryId?: number;
  brandId?: number;
  price: number;
  stock: number;
  conditionLabel?: string;
  imageUrl?: string;
}
