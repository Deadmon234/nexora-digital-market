'use client';

import { MainLayout } from '@/components/layout/MainLayout';
import { ProductGrid } from '@/components/product/ProductGrid';
import { SearchBar } from '@/components/search/SearchBar';
import { Pagination } from '@/components/ui/Pagination';
import { searchProducts } from '@/services/catalog.service';
import type { PageResponse, ProductSummary } from '@/types/product';
import { useCallback, useEffect, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';

export function SearchResults() {
  const searchParams = useSearchParams();
  const router = useRouter();
  const q = searchParams.get('q') ?? '';
  const page = Number(searchParams.get('page') ?? '0');

  const [data, setData] = useState<PageResponse<ProductSummary> | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const load = useCallback(async () => {
    if (!q.trim()) {
      setData(null);
      return;
    }
    setLoading(true);
    setError('');
    try {
      const result = await searchProducts({ q, page, size: 12 });
      setData(result);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erreur de recherche');
    } finally {
      setLoading(false);
    }
  }, [q, page]);

  useEffect(() => {
    load();
  }, [load]);

  function changePage(newPage: number) {
    const params = new URLSearchParams(searchParams.toString());
    params.set('page', String(newPage));
    router.push(`/search?${params.toString()}`);
  }

  return (
    <MainLayout>
      <div className="mx-auto max-w-6xl px-4 py-10">
        <h1 className="text-3xl font-bold text-slate-900">Recherche</h1>
        <div className="mt-6 max-w-xl">
          <SearchBar defaultValue={q} />
        </div>

        {!q.trim() && (
          <p className="mt-8 text-slate-600">Entrez un terme de recherche pour commencer.</p>
        )}

        {loading && <p className="mt-8 text-slate-600">Recherche en cours...</p>}
        {error && <p className="mt-8 text-red-600">{error}</p>}

        {data && !loading && q.trim() && (
          <div className="mt-8">
            <p className="mb-4 text-sm text-slate-500">
              {data.totalElements} résultat(s) pour &quot;{q}&quot;
            </p>
            <ProductGrid products={data.content} />
            <div className="mt-8">
              <Pagination page={data.page} totalPages={data.totalPages} onPageChange={changePage} />
            </div>
          </div>
        )}
      </div>
    </MainLayout>
  );
}
