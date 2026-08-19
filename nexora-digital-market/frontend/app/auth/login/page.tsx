'use client';

import { Logo } from '@/components/brand/Logo';
import { Button } from '@/components/ui/Button';
import { useAuth } from '@/components/providers/AuthProvider';
import { sanitizeEmail } from '@/utils/sanitize';
import { validateEmail, validatePassword } from '@/utils/validation';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { FormEvent, useState } from 'react';

export default function LoginPage() {
  const { login } = useAuth();
  const router = useRouter();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError('');

    const emailError = validateEmail(email);
    if (emailError) {
      setError(emailError);
      return;
    }
    const passwordError = validatePassword(password);
    if (passwordError) {
      setError(passwordError);
      return;
    }

    setLoading(true);
    try {
      const response = await login({ email: sanitizeEmail(email), password });
      const destination = response.user.roles.includes('ROLE_ADMIN')
        ? '/admin'
        : response.user.roles.includes('ROLE_SELLER')
          ? '/seller'
          : '/';
      router.push(destination);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erreur de connexion');
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
            <p className="mt-16 text-xs font-bold uppercase tracking-[0.24em] text-nexora-green-bright">Bienvenue chez Nexora</p>
            <h2 className="display-font mt-4 max-w-sm text-5xl leading-[1.02]">Votre prochain équipement commence ici.</h2>
            <p className="mt-6 max-w-sm text-sm leading-6 text-slate-300">Retrouvez vos commandes, vos favoris et les meilleures offres du marché numérique.</p>
          </div>
          <p className="relative text-xs font-semibold uppercase tracking-[0.18em] text-slate-400">Le numérique, en confiance</p>
        </div>

        <div className="flex items-center justify-center p-6 sm:p-12">
          <div className="w-full max-w-md">
            <div className="mb-8 lg:hidden">
              <Logo size="md" />
            </div>
            <div className="mb-8">
              <p className="text-xs font-bold uppercase tracking-[0.2em] text-nexora-coral">Espace personnel</p>
              <h1 className="mt-3 text-3xl font-bold text-nexora-navy">Bon retour parmi nous</h1>
              <p className="mt-2 text-sm text-slate-600">
                Pas encore de compte ?{' '}
                <Link href="/auth/register" className="font-bold text-nexora-blue hover:text-nexora-blue-dark">
                  Créer un compte
                </Link>
              </p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label htmlFor="email" className="block text-sm font-bold text-nexora-navy">
                Email
              </label>
              <input
                id="email"
                type="email"
                required
                autoComplete="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className={inputClass}
              />
            </div>
            <div>
              <label htmlFor="password" className="block text-sm font-bold text-nexora-navy">
                Mot de passe
              </label>
              <input
                id="password"
                type="password"
                required
                autoComplete="current-password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className={inputClass}
              />
            </div>

            {error && <p className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">{error}</p>}

            <Button type="submit" disabled={loading} className="w-full rounded-xl py-3.5 text-base">
              {loading ? 'Connexion...' : 'Se connecter'}
            </Button>
            </form>
            <p className="mt-8 text-center text-xs text-slate-400">Accès sécurisé à votre compte Nexora</p>
          </div>
        </div>
      </div>
    </main>
  );
}
