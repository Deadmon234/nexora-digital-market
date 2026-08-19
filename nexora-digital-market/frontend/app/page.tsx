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
      <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8 lg:py-12">
        <section className="hero-grid relative overflow-hidden rounded-[2rem] px-6 py-12 text-white shadow-xl sm:px-10 lg:px-16 lg:py-16">
          <div className="absolute -right-16 top-10 h-72 w-72 rounded-full border-[32px] border-nexora-coral/30" />
          <div className="absolute bottom-[-5rem] right-28 h-40 w-40 rounded-full bg-nexora-green/90" />
          <div className="relative grid items-end gap-12 lg:grid-cols-[1.1fr_0.9fr]">
            <div className="max-w-2xl">
              <p className="text-sm font-bold uppercase tracking-[0.24em] text-nexora-green-bright">
                Le marché du numérique, autrement
              </p>
              <h1 className="display-font mt-5 text-5xl leading-[0.98] sm:text-6xl lg:text-7xl">
                La technologie qui suit <span className="text-nexora-green-bright">votre rythme.</span>
              </h1>
              <p className="mt-6 max-w-lg text-base leading-7 text-slate-300 sm:text-lg">
                Comparez les offres de vendeurs vérifiés et trouvez le bon équipement pour travailler, créer et jouer.
              </p>
              <div className="mt-8 max-w-2xl">
                <SearchBar variant="hero" />
              </div>
              <div className="mt-5 flex flex-wrap gap-x-6 gap-y-2 text-xs font-semibold uppercase tracking-wider text-slate-300">
                <span>Vendeurs vérifiés</span><span>Paiement sécurisé</span><span>Livraison suivie</span>
              </div>
            </div>
            <div className="relative hidden min-h-64 lg:block">
              <div className="absolute right-0 top-2 w-64 rotate-3 rounded-2xl bg-nexora-ivory p-5 text-nexora-navy shadow-2xl">
                <p className="text-xs font-bold uppercase tracking-widest text-nexora-coral">Sélection du moment</p>
                <p className="display-font mt-5 text-3xl">Tout ce qu&apos;il faut pour aller plus loin.</p>
                <div className="mt-8 flex items-center justify-between border-t border-slate-200 pt-3 text-xs font-bold">
                  <span>Explorer la sélection</span><span className="text-nexora-coral">↗</span>
                </div>
              </div>
            </div>
          </div>
        </section>

        <section className="mt-10 grid gap-3 border-y border-slate-200 py-5 text-sm sm:grid-cols-3">
          <div><span className="font-bold text-nexora-coral">01</span><span className="ml-3 text-slate-600">Des offres comparées en un clin d&apos;œil</span></div>
          <div><span className="font-bold text-nexora-coral">02</span><span className="ml-3 text-slate-600">Des produits choisis pour durer</span></div>
          <div><span className="font-bold text-nexora-coral">03</span><span className="ml-3 text-slate-600">Un accompagnement avant et après achat</span></div>
        </section>

        <section className="mt-14">
          <div className="mb-6 flex items-center justify-between">
            <div><p className="text-xs font-bold uppercase tracking-[0.2em] text-nexora-coral">Choisir son terrain</p><h2 className="display-font mt-1 text-3xl text-nexora-navy">Explorer par catégorie</h2></div>
            <Link href="/categories" className="text-sm font-bold text-nexora-blue hover:text-nexora-blue-dark">
              Tout voir ↗
            </Link>
          </div>
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {categories.slice(0, 3).map((category) => (
              <CategoryCard key={category.id} category={category} />
            ))}
          </div>
        </section>

        <section className="mt-16">
          <div className="mb-6 flex items-center justify-between">
            <div><p className="text-xs font-bold uppercase tracking-[0.2em] text-nexora-coral">À ne pas manquer</p><h2 className="display-font mt-1 text-3xl text-nexora-navy">Les essentiels du moment</h2></div>
            <Link href="/products" className="text-sm font-bold text-nexora-blue hover:text-nexora-blue-dark">
              Voir le catalogue ↗
            </Link>
          </div>
          <ProductGrid products={products.content} />
        </section>
      </div>
    </MainLayout>
  );
}
