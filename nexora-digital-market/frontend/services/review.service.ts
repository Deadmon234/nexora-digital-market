import { apiRequest } from '@/services/api.service';
import type { CreateReviewPayload, Review, ReviewSummary } from '@/types/review';

export function getProductReviews(slug: string) {
  return apiRequest<ReviewSummary>(`/api/reviews/products/${slug}`);
}

export function createProductReview(slug: string, data: CreateReviewPayload) {
  return apiRequest<Review>(`/api/reviews/products/${slug}`, { method: 'POST', body: data });
}

export function updateProductReview(id: number, data: CreateReviewPayload) {
  return apiRequest<Review>(`/api/reviews/products/${id}`, { method: 'PUT', body: data });
}

export function deleteProductReview(id: number) {
  return apiRequest<void>(`/api/reviews/products/${id}`, { method: 'DELETE' });
}

export function getShopReviews(slug: string) {
  return apiRequest<ReviewSummary>(`/api/reviews/shops/${slug}`);
}

export function createShopReview(slug: string, data: CreateReviewPayload) {
  return apiRequest<Review>(`/api/reviews/shops/${slug}`, { method: 'POST', body: data });
}

export function updateShopReview(id: number, data: CreateReviewPayload) {
  return apiRequest<Review>(`/api/reviews/shops/${id}`, { method: 'PUT', body: data });
}

export function deleteShopReview(id: number) {
  return apiRequest<void>(`/api/reviews/shops/${id}`, { method: 'DELETE' });
}

export function getShop(slug: string) {
  return apiRequest<{ id: number; name: string; slug: string; description?: string }>(`/api/shops/${slug}`);
}
