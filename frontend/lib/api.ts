import type { ApiError, AuthResponse, LoginPayload, RegisterPayload, UserResponse } from './types';

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? 'http://localhost:8080';

export class ApiRequestError extends Error {
  readonly status: number;
  readonly fieldErrors: Record<string, string>;

  constructor(status: number, message: string, fieldErrors: Record<string, string> = {}) {
    super(message);
    this.name = 'ApiRequestError';
    this.status = status;
    this.fieldErrors = fieldErrors;
  }
}

async function request<T>(path: string, init: RequestInit = {}, accessToken?: string): Promise<T> {
  const response = await fetch(`${API_URL}${path}`, {
    ...init,
    headers: {
      'Content-Type': 'application/json',
      ...(accessToken ? { Authorization: `Bearer ${accessToken}` } : {}),
      ...init.headers,
    },
  });

  if (!response.ok) {
    const body = (await response.json().catch(() => null)) as ApiError | null;
    throw new ApiRequestError(
      response.status,
      body?.message ?? 'Une erreur est survenue',
      body?.fieldErrors ?? {},
    );
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return (await response.json()) as T;
}

export const api = {
  register: (payload: RegisterPayload) =>
    request<AuthResponse>('/api/v1/auth/register', { method: 'POST', body: JSON.stringify(payload) }),

  login: (payload: LoginPayload) =>
    request<AuthResponse>('/api/v1/auth/login', { method: 'POST', body: JSON.stringify(payload) }),

  refresh: (refreshToken: string) =>
    request<AuthResponse>('/api/v1/auth/refresh', { method: 'POST', body: JSON.stringify({ refreshToken }) }),

  logout: (accessToken: string) =>
    request<void>('/api/v1/auth/logout', { method: 'POST' }, accessToken),

  me: (accessToken: string) => request<UserResponse>('/api/v1/users/me', {}, accessToken),
};
