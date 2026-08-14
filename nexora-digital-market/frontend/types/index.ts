export type RoleName = 'ROLE_CLIENT' | 'ROLE_SELLER' | 'ROLE_ADMIN';

export interface User {
  id: number;
  email: string;
  firstName?: string;
  lastName?: string;
  phone?: string;
  enabled: boolean;
  roles: RoleName[];
}

export interface ApiHealth {
  status: string;
  service: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
