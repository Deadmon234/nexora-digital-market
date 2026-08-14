import { apiRequest } from '@/services/api.service';
import type { Favorite } from '@/types/client';

export function getFavorites() {
  return apiRequest<Favorite[]>('/api/favorites');
}

export function checkFavorite(productId: number) {
  return apiRequest<{ favorited: boolean }>(`/api/favorites/check/${productId}`);
}

export function addFavorite(productId: number) {
  return apiRequest<Favorite>(`/api/favorites/${productId}`, { method: 'POST' });
}

export function removeFavorite(productId: number) {
  return apiRequest<void>(`/api/favorites/${productId}`, { method: 'DELETE' });
}
