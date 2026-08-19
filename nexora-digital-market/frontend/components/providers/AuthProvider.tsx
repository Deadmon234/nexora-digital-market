'use client';

import { login as loginApi, logout as logoutApi, register as registerApi } from '@/services/auth.service';
import { getProfile } from '@/services/account.service';
import type { AuthResponse, AuthUser, LoginPayload, RegisterPayload } from '@/types/auth';
import { clearTokens, isAuthenticated } from '@/utils/auth-storage';
import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';

interface AuthContextValue {
  user: AuthUser | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  login: (payload: LoginPayload) => Promise<AuthResponse>;
  register: (payload: RegisterPayload) => Promise<AuthResponse>;
  logout: () => Promise<void>;
  setUser: (user: AuthUser | null) => void;
}

export const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    let active = true;

    async function restoreSession() {
      if (!isAuthenticated()) {
        if (active) setIsLoading(false);
        return;
      }

      try {
        const profile = await getProfile();
        if (active) {
          setUser({ ...profile, roles: profile.roles as AuthUser['roles'] });
        }
      } catch {
        if (active) {
          setUser(null);
          clearTokens();
        }
      } finally {
        if (active) setIsLoading(false);
      }
    }

    restoreSession();
    return () => {
      active = false;
    };
  }, []);

  const login = useCallback(async (payload: LoginPayload) => {
    const response = await loginApi(payload);
    setUser(response.user);
    return response;
  }, []);

  const register = useCallback(async (payload: RegisterPayload) => {
    const response = await registerApi(payload);
    setUser(response.user);
    return response;
  }, []);

  const logout = useCallback(async () => {
    await logoutApi();
    setUser(null);
    clearTokens();
  }, []);

  const value = useMemo(
    () => ({
      user,
      isLoading,
      isAuthenticated: !isLoading && (!!user || isAuthenticated()),
      login,
      register,
      logout,
      setUser,
    }),
    [user, isLoading, login, register, logout],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth doit être utilisé dans un AuthProvider');
  }
  return context;
}
