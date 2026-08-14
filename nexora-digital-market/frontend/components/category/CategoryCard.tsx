import Link from 'next/link';
import type { Category } from '@/types/product';

interface CategoryCardProps {
  category: Category;
}

export function CategoryCard({ category }: CategoryCardProps) {
  return (
    <Link
      href={`/categories/${category.slug}`}
      className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm transition hover:border-slate-400 hover:shadow-md"
    >
      <h3 className="text-lg font-semibold text-slate-900">{category.name}</h3>
      {category.description && (
        <p className="mt-2 text-sm text-slate-600">{category.description}</p>
      )}
      {category.children && category.children.length > 0 && (
        <p className="mt-3 text-xs text-slate-500">{category.children.length} sous-catégorie(s)</p>
      )}
    </Link>
  );
}
