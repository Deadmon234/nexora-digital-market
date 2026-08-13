import type { AuthResponse, Role, UserResponse } from './types';

/**
 * Le jeton d'acces est conserve dans un cookie lisible cote middleware afin de router
 * l'utilisateur vers son espace. Les autorisations restent verifiees par le backend.
 */
const ACCESS_TOKEN_COOKIE = 'nexora_access_token';
const ROLE_COOKIE = 'nexora_role';
const USER_STORAGE_KEY = 'nexora_user';

function setCookie(name: string, value: string, maxAgeSeconds: number) {
  document.cookie = `${name}=${encodeURIComponent(value)}; path=/; max-age=${maxAgeSeconds}; samesite=lax`;
}

function clearCookie(name: string) {
  document.cookie = `${name}=; path=/; max-age=0; samesite=lax`;
}

export function persistSession(auth: AuthResponse) {
  setCookie(ACCESS_TOKEN_COOKIE, auth.accessToken, auth.expiresInSeconds);
  setCookie(ROLE_COOKIE, auth.user.role, auth.expiresInSeconds);
  window.localStorage.setItem(USER_STORAGE_KEY, JSON.stringify(auth.user));
}

export function clearSession() {
  clearCookie(ACCESS_TOKEN_COOKIE);
  clearCookie(ROLE_COOKIE);
  window.localStorage.removeItem(USER_STORAGE_KEY);
}

export function readStoredUser(): UserResponse | null {
  const raw = window.localStorage.getItem(USER_STORAGE_KEY);
  return raw ? (JSON.parse(raw) as UserResponse) : null;
}

export function readAccessToken(): string | null {
  const match = document.cookie.match(new RegExp(`(?:^|; )${ACCESS_TOKEN_COOKIE}=([^;]*)`));
  return match ? decodeURIComponent(match[1]) : null;
}

export function homePathForRole(role: Role): string {
  switch (role) {
    case 'ADMIN':
      return '/admin';
    case 'SELLER':
      return '/seller';
    default:
      return '/account';
  }
}

export const cookieNames = { accessToken: ACCESS_TOKEN_COOKIE, role: ROLE_COOKIE };
