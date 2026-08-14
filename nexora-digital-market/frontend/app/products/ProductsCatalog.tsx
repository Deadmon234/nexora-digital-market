'use client';

import { FilterPanel } from '@/components/search/FilterPanel';
import { MainLayout } from '@/components/layout/MainLayout';
import { ProductGrid } from '@/components/product/ProductGrid';
import { Pagination } from '@/components/ui/Pagination';
import { getBrands, getCategories, getProducts } from '@/services/catalog.service';
import type { Brand, Category, PageResponse, ProductSummary } from '@/types/product';
import { useCallback, useEffect, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';

export function ProductsCatalog() {
  const router = useRouter();
  const searchParams = useSearchParams();

  const [categories, setCategories] = useState<Category[]>([]);
  const [brands, setBrands] = useState<Brand[]>([]);
  const [data, setData] = useState<PageResponse<ProductSummary> | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const category = searchParams.get('category') ?? undefined;
  const brand = searchParams.get('brand') ?? undefined;
  const sort = searchParams.get('sort') ?? 'newest';
  const page = Number(searchParams.get('page') ?? '0');

  const load = useCallback(async () => {
    setLoading(true);
    setError('');
    try {
      const result = await getProducts({ category, brand, sort, page, size: 12 });
      setData(result);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erreur de chargement');
    } finally {
      setLoading(false);
    }
  }, [category, brand, sort, page]);

  useEffect(() => {
    getCategories().then(setCategories).catch(() => {});
    getBrands().then(setBrands).catch(() => {});
  }, []);

  useEffect(() => {
    load();
  }, [load]);

  function updateFilters(filters: { category?: string; brand?: string; sort?: string }) {
    const params = new URLSearchParams();
    if (filters.category) params.set('category', filters.category);
    if (filters.brand) params.set('brand', filters.brand);
    if (filters.sort && filters.sort !== 'newest') params.set('sort', filters.sort);
    router.push(`/products?${params.toString()}`);
  }

  function changePage(newPage: number) {
    const params = new URLSearchParams(searchParams.toString());
    params.set('page', String(newPage));
    router.push(`/products?${params.toString()}`);
  }

  return (
    <MainLayout>
      <div className="mx-auto max-w-6xl px-4 py-10">
        <h1 className="text-3xl font-bold text-slate-900">Catalogue</h1>
        <p className="mt-2 text-slate-600">Parcourez tous les produits disponibles.</p>

        <div className="mt-8 grid gap-8 lg:grid-cols-[240px_1fr]">
          <FilterPanel
            categories={categories}
            brands={brands}
            selectedCategory={category}
            selectedBrand={brand}
            selectedSort={sort}
            onChange={updateFilters}
          />
          <div>
            {loading && <p className="text-slate-600">Chargement...</p>}
            {error && <p className="text-red-600">{error}</p>}
            {data && !loading && (
              <>
                <p className="mb-4 text-sm text-slate-500">{data.totalElements} produit(s)</p>
                <ProductGrid products={data.content} />
                <div className="mt-8">
                  <Pagination page={data.page} totalPages={data.totalPages} onPageChange={changePage} />
                </div>
              </>
            )}
          </div>
        </div>
      </div>
    </MainLayout>
  );
}
