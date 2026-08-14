import { MainLayout } from '@/components/layout/MainLayout';
import { SearchResults } from '@/app/search/SearchResults';
import { Suspense } from 'react';

export default function SearchPage() {
  return (
    <Suspense
      fallback={
        <MainLayout>
          <div className="mx-auto max-w-6xl px-4 py-10 text-slate-600">Chargement...</div>
        </MainLayout>
      }
    >
      <SearchResults />
    </Suspense>
  );
}
