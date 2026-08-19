import Link from 'next/link';
import type { Category } from '@/types/product';

interface CategoryCardProps {
  category: Category;
}

export function CategoryCard({ category }: CategoryCardProps) {
  return (
    <Link
      href={`/categories/${category.slug}`}
      className="group relative overflow-hidden rounded-xl border border-slate-200/80 bg-white p-6 shadow-sm transition duration-300 hover:-translate-y-1 hover:border-nexora-blue/40 hover:shadow-lg"
    >
      <span className="mb-8 block text-3xl font-light text-nexora-coral">0{category.id}</span>
      <h3 className="text-lg font-bold text-slate-900 group-hover:text-nexora-blue">{category.name}</h3>
      {category.description && (
        <p className="mt-2 text-sm text-slate-600">{category.description}</p>
      )}
      {category.children && category.children.length > 0 && (
        <p className="mt-3 text-xs text-slate-500">{category.children.length} sous-catégorie(s)</p>
      )}
    </Link>
  );
}
