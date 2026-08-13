import Link from 'next/link';
import { Suspense } from 'react';
import { AuthForm } from '../../../components/auth/AuthForm';

export default function LoginPage() {
  return (
    <main className="mx-auto flex min-h-screen max-w-md flex-col justify-center px-6 py-16">
      <h1 className="text-2xl font-bold text-slate-900">Connexion</h1>
      <p className="mt-2 text-sm text-slate-600">Accedez a votre espace client, vendeur ou administrateur.</p>
      <div className="mt-8">
        <Suspense>
          <AuthForm mode="login" />
        </Suspense>
      </div>
      <p className="mt-6 text-sm text-slate-600">
        Pas encore de compte ?{' '}
        <Link href="/register" className="font-medium text-slate-900 underline">
          Creer un compte
        </Link>
      </p>
    </main>
  );
}
