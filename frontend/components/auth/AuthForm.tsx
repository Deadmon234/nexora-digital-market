'use client';

import { useRouter, useSearchParams } from 'next/navigation';
import { useState } from 'react';
import type { FormEvent } from 'react';
import { ApiRequestError, api } from '../../lib/api';
import { homePathForRole, persistSession } from '../../lib/session';
import { Button } from '../ui/Button';
import { TextField } from '../ui/TextField';

type Mode = 'login' | 'register';

export function AuthForm({ mode }: { mode: Mode }) {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setSubmitting(true);
    setError(null);
    setFieldErrors({});

    const data = new FormData(event.currentTarget);
    try {
      const auth =
        mode === 'login'
          ? await api.login({
              email: String(data.get('email')),
              password: String(data.get('password')),
            })
          : await api.register({
              email: String(data.get('email')),
              password: String(data.get('password')),
              firstName: String(data.get('firstName')),
              lastName: String(data.get('lastName')),
              phone: String(data.get('phone') ?? ''),
            });

      persistSession(auth);
      router.push(searchParams.get('next') ?? homePathForRole(auth.user.role));
      router.refresh();
    } catch (caught) {
      if (caught instanceof ApiRequestError) {
        setError(caught.message);
        setFieldErrors(caught.fieldErrors);
      } else {
        setError('Le serveur est injoignable');
      }
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form onSubmit={onSubmit} className="flex flex-col gap-4">
      {mode === 'register' ? (
        <>
          <TextField label="Prenom" name="firstName" required error={fieldErrors.firstName} />
          <TextField label="Nom" name="lastName" required error={fieldErrors.lastName} />
          <TextField label="Telephone" name="phone" error={fieldErrors.phone} />
        </>
      ) : null}
      <TextField label="Adresse e-mail" name="email" type="email" required error={fieldErrors.email} />
      <TextField
        label="Mot de passe"
        name="password"
        type="password"
        required
        minLength={8}
        error={fieldErrors.password}
      />
      {error ? <p className="text-sm text-red-600">{error}</p> : null}
      <Button type="submit" disabled={submitting}>
        {mode === 'login' ? 'Se connecter' : 'Creer mon compte'}
      </Button>
    </form>
  );
}
