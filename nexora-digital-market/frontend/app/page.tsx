import { CategoryCard } from '@/components/category/CategoryCard';
import { Logo } from '@/components/brand/Logo';
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
        <section className="relative overflow-hidden rounded-3xl bg-nexora-hero px-8 py-12 text-white shadow-lg">
          <div className="pointer-events-none absolute -right-16 -top-16 h-64 w-64 rounded-full bg-nexora-green/20 blur-3xl" />
          <div className="pointer-events-none absolute -bottom-20 -left-10 h-56 w-56 rounded-full bg-nexora-blue/30 blur-3xl" />
          <div className="relative flex flex-col items-start gap-8 lg:flex-row lg:items-center lg:justify-between">
            <div className="max-w-xl">
              <p className="text-sm font-semibold uppercase tracking-widest text-nexora-green-bright">
                Nexora Digital Market
              </p>
              <h1 className="mt-2 text-4xl font-bold leading-tight sm:text-5xl">
                Construisons votre{' '}
                <span className="text-nexora-green-bright">avenir numérique</span>
              </h1>
              <p className="mt-4 text-lg text-blue-100">
                Marketplace multi-vendeurs — smartphones, ordinateurs et électronique.
              </p>
              <div className="mt-8 max-w-xl">
                <SearchBar variant="hero" />
              </div>
            </div>
            <div className="hidden shrink-0 rounded-2xl bg-white/10 p-4 backdrop-blur-sm lg:block">
              <Logo size="lg" href={undefined} className="brightness-110 drop-shadow-lg" />
            </div>
          </div>
        </section>

        <section className="mt-12">
          <div className="mb-6 flex items-center justify-between">
            <h2 className="text-2xl font-bold text-nexora-navy">Catégories</h2>
            <Link href="/categories" className="text-sm font-medium text-nexora-blue hover:text-nexora-blue-dark">
              Voir tout →
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
            <h2 className="text-2xl font-bold text-nexora-navy">Produits populaires</h2>
            <Link href="/products" className="text-sm font-medium text-nexora-blue hover:text-nexora-blue-dark">
              Voir le catalogue →
            </Link>
          </div>
          <ProductGrid products={products.content} />
        </section>
      </div>
    </MainLayout>
  );
}
