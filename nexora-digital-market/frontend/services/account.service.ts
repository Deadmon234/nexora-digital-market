import { apiRequest } from '@/services/api.service';
import type { Address, AddressPayload, UserProfile } from '@/types/client';

export function getProfile() {
  return apiRequest<UserProfile>('/api/users/me');
}

export function updateProfile(data: Partial<UserProfile>) {
  return apiRequest<UserProfile>('/api/users/me', { method: 'PUT', body: data });
}

export function getAddresses() {
  return apiRequest<Address[]>('/api/addresses');
}

export function createAddress(data: AddressPayload) {
  return apiRequest<Address>('/api/addresses', { method: 'POST', body: data });
}

export function updateAddress(id: number, data: AddressPayload) {
  return apiRequest<Address>(`/api/addresses/${id}`, { method: 'PUT', body: data });
}

export function deleteAddress(id: number) {
  return apiRequest<void>(`/api/addresses/${id}`, { method: 'DELETE' });
}

export function setDefaultAddress(id: number) {
  return apiRequest<Address>(`/api/addresses/${id}/default`, { method: 'PUT' });
}
