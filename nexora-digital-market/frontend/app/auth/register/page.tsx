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
    <main className="auth-stage min-h-screen px-3 py-3 sm:px-6 sm:py-6 lg:px-10">
      <div className="mx-auto grid min-h-[calc(100vh-1.5rem)] w-full max-w-7xl overflow-hidden rounded-[2rem] bg-white shadow-[0_28px_100px_rgba(9,42,92,0.18)] lg:min-h-[calc(100vh-3rem)] lg:grid-cols-[1.05fr_0.95fr]">
        <div className="auth-panel-pattern relative hidden overflow-hidden p-8 text-white lg:flex lg:flex-col lg:justify-between lg:p-12">
          <div className="absolute -bottom-28 -right-20 h-[28rem] w-[28rem] rounded-full border-[2px] border-nexora-green/40" />
          <div className="absolute -bottom-16 -right-8 h-80 w-80 rounded-full border-[18px] border-nexora-green/20" />
          <div className="absolute right-24 top-24 h-20 w-20 rounded-full bg-nexora-green/90 shadow-[0_0_0_12px_rgba(97,227,62,0.1)]" />
          <div className="absolute left-12 top-1/2 h-2 w-24 bg-nexora-coral" />
          <div className="relative">
            <div className="flex items-center justify-between">
              <div className="inline-flex rounded-2xl bg-white px-4 py-3 shadow-xl">
              <Logo size="sm" />
              </div>
              <span className="text-xs font-bold uppercase tracking-[0.24em] text-slate-400">N / 02</span>
            </div>
            <p className="mt-20 text-xs font-bold uppercase tracking-[0.24em] text-nexora-green-bright">Votre compte shopping</p>
            <h2 className="display-font mt-4 max-w-lg text-6xl leading-[0.94]">Une meilleure façon de faire vos achats.</h2>
            <p className="mt-7 max-w-sm text-sm leading-6 text-slate-300">Créez votre espace pour comparer les produits, enregistrer vos favoris et suivre chaque livraison.</p>
            <div className="mt-12 grid max-w-sm grid-cols-2 gap-3">
              <div className="rounded-2xl border border-white/15 bg-white/10 p-4"><p className="text-2xl font-bold text-nexora-green-bright">♡</p><p className="mt-2 text-xs leading-5 text-slate-300">Vos favoris toujours à portée</p></div>
              <div className="rounded-2xl border border-white/15 bg-white/10 p-4"><p className="text-2xl font-bold text-nexora-green-bright">24/7</p><p className="mt-2 text-xs leading-5 text-slate-300">Votre catalogue, quand vous voulez</p></div>
            </div>
          </div>
          <div className="relative flex items-end justify-between text-xs font-semibold uppercase tracking-[0.18em] text-slate-400"><span>Simple. Fiable. Numérique.</span><span>2026</span></div>
        </div>

        <div className="flex items-center justify-center p-6 sm:p-12">
          <div className="w-full max-w-md">
            <div className="mb-8 lg:hidden">
              <Logo size="md" />
            </div>
            <div className="mb-8">
              <p className="text-xs font-bold uppercase tracking-[0.2em] text-nexora-coral">Mon compte / Inscription</p>
              <h1 className="mt-3 text-4xl font-bold tracking-tight text-nexora-navy">Créez votre compte shopping</h1>
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
            <div className="mt-8 flex flex-wrap items-center justify-center gap-x-4 gap-y-2 text-xs text-slate-400"><span className="flex items-center gap-2"><span className="h-1.5 w-1.5 rounded-full bg-nexora-green" /> Données protégées</span><span>•</span><span>Livraison suivie</span></div>
          </div>
        </div>
      </div>
    </main>
  );
}
