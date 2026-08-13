'use client';

import { useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import { api } from '../../lib/api';
import { clearSession, readAccessToken } from '../../lib/session';
import type { UserResponse } from '../../lib/types';
import { Button } from '../ui/Button';
import { Card } from '../ui/Card';

export function SessionSummary() {
  const router = useRouter();
  const [user, setUser] = useState<UserResponse | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const token = readAccessToken();
    if (!token) {
      router.push('/login');
      return;
    }
    api
      .me(token)
      .then(setUser)
      .catch(() => setError('Session expiree, reconnectez-vous'));
  }, [router]);

  async function onLogout() {
    const token = readAccessToken();
    if (token) {
      await api.logout(token).catch(() => undefined);
    }
    clearSession();
    router.push('/login');
    router.refresh();
  }

  return (
    <Card title="Mon compte">
      {error ? <p className="text-sm text-red-600">{error}</p> : null}
      {user ? (
        <dl className="grid gap-2 text-sm text-slate-700">
          <div className="flex gap-2">
            <dt className="font-medium">Nom :</dt>
            <dd>
              {user.firstName} {user.lastName}
            </dd>
          </div>
          <div className="flex gap-2">
            <dt className="font-medium">E-mail :</dt>
            <dd>{user.email}</dd>
          </div>
          <div className="flex gap-2">
            <dt className="font-medium">Role :</dt>
            <dd>{user.role}</dd>
          </div>
        </dl>
      ) : null}
      <div className="mt-4">
        <Button variant="secondary" onClick={onLogout}>
          Se deconnecter
        </Button>
      </div>
    </Card>
  );
}
