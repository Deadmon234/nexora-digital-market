import { MainLayout } from '@/components/layout/MainLayout';
import { ProductsCatalog } from '@/app/products/ProductsCatalog';
import { Suspense } from 'react';

export default function ProductsPage() {
  return (
    <Suspense
      fallback={
        <MainLayout>
          <div className="mx-auto max-w-6xl px-4 py-10 text-slate-600">Chargement...</div>
        </MainLayout>
      }
    >
      <ProductsCatalog />
    </Suspense>
  );
}
