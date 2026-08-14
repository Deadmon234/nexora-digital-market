import { CategoryCard } from '@/components/category/CategoryCard';
import { MainLayout } from '@/components/layout/MainLayout';
import { ProductGrid } from '@/components/product/ProductGrid';
import { SearchBar } from '@/components/search/SearchBar';
import { API_BASE_URL } from '@/utils/constants';
import type { Category, PageResponse, ProductSummary } from '@/types/product';
import Link from 'next/link';

export const dynamic = 'force-dynamic';

async function fetchJson<T>(path: string, fallback: T): Promise<T> {
  try {
    const res = await fetch(`${API_BASE_URL}${path}`, { cache: 'no-store' });
    if (!res.ok) return fallback;
    return res.json() as Promise<T>;
  } catch {
    return fallback;
  }
}

export default async function HomePage() {
  const emptyPage: PageResponse<ProductSummary> = {
    content: [],
    page: 0,
    size: 6,
    totalElements: 0,
    totalPages: 0,
  };

  const [products, categories] = await Promise.all([
    fetchJson<PageResponse<ProductSummary>>('/api/products?size=6', emptyPage),
    fetchJson<Category[]>('/api/categories', []),
  ]);

  return (
    <MainLayout>
      <div className="mx-auto max-w-6xl px-4 py-12">
        <section className="rounded-3xl bg-slate-900 px-8 py-12 text-white">
          <h1 className="text-4xl font-bold">Nexora Digital Market</h1>
          <p className="mt-4 max-w-2xl text-slate-300">
            Marketplace e-commerce multi-vendeurs — smartphones, ordinateurs et électronique.
          </p>
          <div className="mt-8 max-w-xl">
            <SearchBar />
          </div>
        </section>

        <section className="mt-12">
          <div className="mb-6 flex items-center justify-between">
            <h2 className="text-2xl font-bold text-slate-900">Catégories</h2>
            <Link href="/categories" className="text-sm text-slate-600 hover:text-slate-900">
              Voir tout
            </Link>
          </div>
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {categories.slice(0, 3).map((category) => (
              <CategoryCard key={category.id} category={category} />
            ))}
          </div>
        </section>

        <section className="mt-12">
          <div className="mb-6 flex items-center justify-between">
            <h2 className="text-2xl font-bold text-slate-900">Produits populaires</h2>
            <Link href="/products" className="text-sm text-slate-600 hover:text-slate-900">
              Voir le catalogue
            </Link>
          </div>
          <ProductGrid products={products.content} />
        </section>
      </div>
    </MainLayout>
  );
}
