'use client';

import { Logo } from '@/components/brand/Logo';
import { Button } from '@/components/ui/Button';
import { useAuth } from '@/components/providers/AuthProvider';
import { sanitizeEmail, sanitizeText } from '@/utils/sanitize';
import { validateEmail, validateName, validatePassword } from '@/utils/validation';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { FormEvent, useState } from 'react';

export default function RegisterPage() {
  const { register } = useAuth();
  const router = useRouter();
  const [form, setForm] = useState({
    email: '',
    password: '',
    firstName: '',
    lastName: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError('');

    const firstNameError = validateName(form.firstName, 'Prénom');
    if (firstNameError) {
      setError(firstNameError);
      return;
    }
    const lastNameError = validateName(form.lastName, 'Nom');
    if (lastNameError) {
      setError(lastNameError);
      return;
    }
    const emailError = validateEmail(form.email);
    if (emailError) {
      setError(emailError);
      return;
    }
    const passwordError = validatePassword(form.password);
    if (passwordError) {
      setError(passwordError);
      return;
    }

    setLoading(true);
    try {
      await register({
        email: sanitizeEmail(form.email),
        password: form.password,
        firstName: sanitizeText(form.firstName),
        lastName: sanitizeText(form.lastName),
      });
      router.push('/');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erreur d\'inscription');
    } finally {
      setLoading(false);
    }
  }

  const inputClass =
    'mt-2 w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-nexora-navy outline-none transition placeholder:text-slate-400 focus:border-nexora-blue focus:bg-white focus:ring-4 focus:ring-nexora-blue/10';

  return (
    <main className="min-h-screen bg-[#edf3f8] px-4 py-6 sm:px-8 sm:py-10">
      <div className="mx-auto grid min-h-[calc(100vh-3rem)] w-full max-w-6xl overflow-hidden rounded-[2rem] bg-white shadow-[0_24px_80px_rgba(9,42,92,0.14)] lg:grid-cols-[0.9fr_1.1fr]">
        <div className="relative hidden overflow-hidden bg-nexora-navy p-12 text-white lg:flex lg:flex-col lg:justify-between">
          <div className="absolute -bottom-20 -right-14 h-72 w-72 rounded-full border-[36px] border-nexora-green/25" />
          <div className="absolute right-16 top-28 h-16 w-16 rounded-full bg-nexora-green/80" />
          <div className="relative">
            <div className="inline-flex rounded-2xl bg-white px-4 py-3">
              <Logo size="sm" />
            </div>
            <p className="mt-16 text-xs font-bold uppercase tracking-[0.24em] text-nexora-green-bright">Rejoignez Nexora</p>
            <h2 className="display-font mt-4 max-w-sm text-5xl leading-[1.02]">Faites grandir vos projets numériques.</h2>
            <p className="mt-6 max-w-sm text-sm leading-6 text-slate-300">Un compte pour acheter simplement, suivre vos commandes et découvrir un marché qui vous ressemble.</p>
          </div>
          <p className="relative text-xs font-semibold uppercase tracking-[0.18em] text-slate-400">Simple. Fiable. Numérique.</p>
        </div>

        <div className="flex items-center justify-center p-6 sm:p-12">
          <div className="w-full max-w-md">
            <div className="mb-8 lg:hidden">
              <Logo size="md" />
            </div>
            <div className="mb-8">
              <p className="text-xs font-bold uppercase tracking-[0.2em] text-nexora-coral">Créer votre espace</p>
              <h1 className="mt-3 text-3xl font-bold text-nexora-navy">Bienvenue dans l&apos;aventure</h1>
              <p className="mt-2 text-sm text-slate-600">
                Déjà un compte ?{' '}
                <Link href="/auth/login" className="font-bold text-nexora-blue hover:text-nexora-blue-dark">
                  Se connecter
                </Link>
              </p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-5">
            <div className="grid gap-4 sm:grid-cols-2">
              <div>
                <label htmlFor="firstName" className="block text-sm font-bold text-nexora-navy">
                  Prénom
                </label>
                <input
                  id="firstName"
                  autoComplete="given-name"
                  value={form.firstName}
                  onChange={(e) => setForm({ ...form, firstName: e.target.value })}
                  className={inputClass}
                />
              </div>
              <div>
                <label htmlFor="lastName" className="block text-sm font-bold text-nexora-navy">
                  Nom
                </label>
                <input
                  id="lastName"
                  autoComplete="family-name"
                  value={form.lastName}
                  onChange={(e) => setForm({ ...form, lastName: e.target.value })}
                  className={inputClass}
                />
              </div>
            </div>
            <div>
              <label htmlFor="email" className="block text-sm font-bold text-nexora-navy">
                Email
              </label>
              <input
                id="email"
                type="email"
                required
                autoComplete="email"
                value={form.email}
                onChange={(e) => setForm({ ...form, email: e.target.value })}
                className={inputClass}
              />
            </div>
            <div>
              <label htmlFor="password" className="block text-sm font-bold text-nexora-navy">
                Mot de passe (min. 8 caractères)
              </label>
              <input
                id="password"
                type="password"
                required
                minLength={8}
                autoComplete="new-password"
                value={form.password}
                onChange={(e) => setForm({ ...form, password: e.target.value })}
                className={inputClass}
              />
            </div>

            {error && <p className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">{error}</p>}

            <Button type="submit" disabled={loading} className="w-full rounded-xl py-3.5 text-base">
              {loading ? 'Inscription...' : 'Créer mon compte'}
            </Button>
            </form>
            <p className="mt-8 text-center text-xs text-slate-400">Vos informations restent protégées par Nexora</p>
          </div>
        </div>
      </div>
    </main>
  );
}
