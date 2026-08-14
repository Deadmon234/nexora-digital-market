import { MainLayout } from '@/components/layout/MainLayout';
import { ProductGrid } from '@/components/product/ProductGrid';
import { API_BASE_URL } from '@/utils/constants';
import type { Category, PageResponse, ProductSummary } from '@/types/product';
import Link from 'next/link';
import { notFound } from 'next/navigation';

interface Props {
  params: { slug: string };
}

export const dynamic = 'force-dynamic';

export default async function CategoryProductsPage({ params }: Props) {
  let category: Category;
  let products: PageResponse<ProductSummary>;

  try {
    const [catRes, prodRes] = await Promise.all([
      fetch(`${API_BASE_URL}/api/categories/${params.slug}`, { cache: 'no-store' }),
      fetch(`${API_BASE_URL}/api/products?category=${params.slug}&size=12`, { cache: 'no-store' }),
    ]);
    if (!catRes.ok) notFound();
    category = await catRes.json();
    products = prodRes.ok
      ? await prodRes.json()
      : { content: [], page: 0, size: 12, totalElements: 0, totalPages: 0 };
  } catch {
    notFound();
  }

  return (
    <MainLayout>
      <div className="mx-auto max-w-6xl px-4 py-10">
        <nav className="mb-4 text-sm text-slate-500">
          <Link href="/categories" className="hover:text-slate-900">
            Catégories
          </Link>
          {' / '}
          <span className="text-slate-900">{category.name}</span>
        </nav>
        <h1 className="text-3xl font-bold text-slate-900">{category.name}</h1>
        {category.description && <p className="mt-2 text-slate-600">{category.description}</p>}

        {category.children && category.children.length > 0 && (
          <div className="mt-6 flex flex-wrap gap-2">
            {category.children.map((child) => (
              <Link
                key={child.id}
                href={`/categories/${child.slug}`}
                className="rounded-full border border-slate-300 px-3 py-1 text-sm hover:bg-white"
              >
                {child.name}
              </Link>
            ))}
          </div>
        )}

        <div className="mt-10">
          <ProductGrid products={products.content} emptyMessage="Aucun produit dans cette catégorie." />
        </div>
      </div>
    </MainLayout>
  );
}
