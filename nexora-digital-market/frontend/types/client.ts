export interface CartItem {
  id: number;
  offerId: number;
  productId: number;
  productName: string;
  productSlug: string;
  sellerName: string;
  imageUrl?: string;
  quantity: number;
  unitPrice: number;
  lineTotal: number;
  availableStock: number;
}

export interface Cart {
  id: number;
  items: CartItem[];
  itemCount: number;
  totalAmount: number;
}

export interface Address {
  id: number;
  label: string;
  street: string;
  city: string;
  postalCode: string;
  country: string;
  defaultAddress: boolean;
}

export interface AddressPayload {
  label: string;
  street: string;
  city: string;
  postalCode: string;
  country: string;
  defaultAddress?: boolean;
}

export interface Favorite {
  id: number;
  productId: number;
  productName: string;
  productSlug: string;
  imageUrl?: string;
  minPrice?: number;
}

export interface UserProfile {
  id: number;
  email: string;
  firstName?: string;
  lastName?: string;
  phone?: string;
  roles: string[];
}
