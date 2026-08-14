export interface Category {
  id: number;
  name: string;
  slug: string;
  description?: string;
  parentId?: number;
  children?: Category[];
}

export interface Brand {
  id: number;
  name: string;
  slug: string;
  description?: string;
  logoUrl?: string;
}

export interface ProductSummary {
  id: number;
  name: string;
  slug: string;
  description?: string;
  categoryName?: string;
  categorySlug?: string;
  brandName?: string;
  brandSlug?: string;
  imageUrl?: string;
  minPrice?: number;
  offerCount?: number;
}

export interface ProductImage {
  id: number;
  url: string;
  altText?: string;
  displayOrder: number;
  primary: boolean;
}

export interface ProductOffer {
  id: number;
  sellerId: number;
  sellerName: string;
  price: number;
  stock: number;
  conditionLabel?: string;
}

export interface ProductDetail {
  id: number;
  name: string;
  slug: string;
  description?: string;
  category?: Category;
  brand?: Brand;
  images: ProductImage[];
  offers: ProductOffer[];
  minPrice?: number;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface ProductFilters {
  q?: string;
  category?: string;
  brand?: string;
  minPrice?: number;
  maxPrice?: number;
  page?: number;
  size?: number;
  sort?: string;
}
