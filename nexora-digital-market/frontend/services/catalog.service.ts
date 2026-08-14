import { apiRequest } from '@/services/api.service';
import type {
  Brand,
  Category,
  PageResponse,
  ProductDetail,
  ProductFilters,
  ProductSummary,
} from '@/types/product';

function buildQuery(filters: ProductFilters): string {
  const params = new URLSearchParams();
  if (filters.q) params.set('q', filters.q);
  if (filters.category) params.set('category', filters.category);
  if (filters.brand) params.set('brand', filters.brand);
  if (filters.minPrice != null) params.set('minPrice', String(filters.minPrice));
  if (filters.maxPrice != null) params.set('maxPrice', String(filters.maxPrice));
  if (filters.page != null) params.set('page', String(filters.page));
  if (filters.size != null) params.set('size', String(filters.size));
  if (filters.sort) params.set('sort', filters.sort);
  const query = params.toString();
  return query ? `?${query}` : '';
}

export function getProducts(filters: ProductFilters = {}) {
  return apiRequest<PageResponse<ProductSummary>>(`/api/products${buildQuery(filters)}`);
}

export function searchProducts(filters: ProductFilters & { q: string }) {
  return apiRequest<PageResponse<ProductSummary>>(`/api/search${buildQuery(filters)}`);
}

export function getProduct(slug: string) {
  return apiRequest<ProductDetail>(`/api/products/${slug}`);
}

export function getCategories() {
  return apiRequest<Category[]>('/api/categories');
}

export function getCategory(slug: string) {
  return apiRequest<Category>(`/api/categories/${slug}`);
}

export function getBrands() {
  return apiRequest<Brand[]>('/api/brands');
}

export function formatPrice(price?: number): string {
  if (price == null) return '—';
  return new Intl.NumberFormat('fr-FR', { style: 'currency', currency: 'EUR' }).format(price);
}
