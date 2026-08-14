import { apiRequest } from '@/services/api.service';
import type { AuthResponse, LoginPayload, RegisterPayload } from '@/types/auth';
import { clearTokens, getRefreshToken, setTokens } from '@/utils/auth-storage';

export async function register(payload: RegisterPayload): Promise<AuthResponse> {
  const response = await apiRequest<AuthResponse>('/api/auth/register', {
    method: 'POST',
    body: payload,
  });
  setTokens(response.accessToken, response.refreshToken);
  return response;
}

export async function login(payload: LoginPayload): Promise<AuthResponse> {
  const response = await apiRequest<AuthResponse>('/api/auth/login', {
    method: 'POST',
    body: payload,
  });
  setTokens(response.accessToken, response.refreshToken);
  return response;
}

export async function refreshAccessToken(): Promise<AuthResponse | null> {
  const refreshToken = getRefreshToken();
  if (!refreshToken) return null;

  try {
    const response = await apiRequest<AuthResponse>('/api/auth/refresh', {
      method: 'POST',
      body: { refreshToken },
    });
    setTokens(response.accessToken, response.refreshToken);
    return response;
  } catch {
    clearTokens();
    return null;
  }
}

export async function logout(): Promise<void> {
  const refreshToken = getRefreshToken();
  if (refreshToken) {
    try {
      await apiRequest<void>('/api/auth/logout', {
        method: 'POST',
        body: { refreshToken },
      });
    } catch {
      // ignore logout errors
    }
  }
  clearTokens();
}
