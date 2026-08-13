import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

const ROLE_COOKIE = 'nexora_role';
const ACCESS_TOKEN_COOKIE = 'nexora_access_token';

const PROTECTED_PREFIXES: { prefix: string; roles: string[] }[] = [
  { prefix: '/admin', roles: ['ADMIN'] },
  { prefix: '/seller', roles: ['SELLER', 'ADMIN'] },
  { prefix: '/account', roles: ['CLIENT', 'SELLER', 'ADMIN'] },
];

/**
 * Routage d'interface uniquement : evite d'afficher un espace inaccessible.
 * L'autorisation reelle est appliquee par le backend sur chaque appel d'API.
 */
export function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl;
  const rule = PROTECTED_PREFIXES.find((entry) => pathname.startsWith(entry.prefix));
  if (!rule) {
    return NextResponse.next();
  }

  const token = request.cookies.get(ACCESS_TOKEN_COOKIE)?.value;
  const role = request.cookies.get(ROLE_COOKIE)?.value;

  if (!token || !role) {
    const loginUrl = new URL('/login', request.url);
    loginUrl.searchParams.set('next', pathname);
    return NextResponse.redirect(loginUrl);
  }

  if (!rule.roles.includes(role)) {
    return NextResponse.redirect(new URL('/', request.url));
  }

  return NextResponse.next();
}

export const config = {
  matcher: ['/admin/:path*', '/seller/:path*', '/account/:path*'],
};
