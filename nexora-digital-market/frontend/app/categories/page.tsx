import { CategoryCard } from '@/components/category/CategoryCard';
import { MainLayout } from '@/components/layout/MainLayout';
import { API_BASE_URL } from '@/utils/constants';
import type { Category } from '@/types/product';

export const dynamic = 'force-dynamic';

async function getCategories(): Promise<Category[]> {
  try {
    const res = await fetch(`${API_BASE_URL}/api/categories`, { cache: 'no-store' });
    if (!res.ok) return [];
    return res.json();
  } catch {
    return [];
  }
}

export default async function CategoriesPage() {
  const categories = await getCategories();

  return (
    <MainLayout>
      <div className="mx-auto max-w-6xl px-4 py-10">
        <h1 className="text-3xl font-bold text-slate-900">Catégories</h1>
        <p className="mt-2 text-slate-600">Explorez nos catégories de produits.</p>
        <div className="mt-8 grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {categories.map((category) => (
            <CategoryCard key={category.id} category={category} />
          ))}
        </div>
      </div>
    </MainLayout>
  );
}
