import Link from 'next/link';
import { Suspense } from 'react';
import { AuthForm } from '../../../components/auth/AuthForm';

export default function RegisterPage() {
  return (
    <main className="mx-auto flex min-h-screen max-w-md flex-col justify-center px-6 py-16">
      <h1 className="text-2xl font-bold text-slate-900">Creer un compte</h1>
      <p className="mt-2 text-sm text-slate-600">
        Toute inscription cree un compte client. Vous pourrez ensuite demander a devenir vendeur.
      </p>
      <div className="mt-8">
        <Suspense>
          <AuthForm mode="register" />
        </Suspense>
      </div>
      <p className="mt-6 text-sm text-slate-600">
        Deja inscrit ?{' '}
        <Link href="/login" className="font-medium text-slate-900 underline">
          Se connecter
        </Link>
      </p>
    </main>
  );
}
