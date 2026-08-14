'use client';

import type { Brand, Category } from '@/types/product';

interface FilterPanelProps {
  categories: Category[];
  brands: Brand[];
  selectedCategory?: string;
  selectedBrand?: string;
  selectedSort?: string;
  onChange: (filters: { category?: string; brand?: string; sort?: string }) => void;
}

export function FilterPanel({
  categories,
  brands,
  selectedCategory,
  selectedBrand,
  selectedSort = 'newest',
  onChange,
}: FilterPanelProps) {
  return (
    <div className="space-y-4 rounded-2xl border border-slate-200 bg-white p-4">
      <div>
        <label htmlFor="sort" className="block text-sm font-medium text-slate-700">
          Trier par
        </label>
        <select
          id="sort"
          value={selectedSort}
          onChange={(e) => onChange({ category: selectedCategory, brand: selectedBrand, sort: e.target.value })}
          className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm"
        >
          <option value="newest">Plus récents</option>
          <option value="name_asc">Nom A-Z</option>
          <option value="name_desc">Nom Z-A</option>
        </select>
      </div>
      <div>
        <label htmlFor="category" className="block text-sm font-medium text-slate-700">
          Catégorie
        </label>
        <select
          id="category"
          value={selectedCategory ?? ''}
          onChange={(e) =>
            onChange({
              category: e.target.value || undefined,
              brand: selectedBrand,
              sort: selectedSort,
            })
          }
          className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm"
        >
          <option value="">Toutes</option>
          {categories.map((c) => (
            <option key={c.id} value={c.slug}>
              {c.name}
            </option>
          ))}
        </select>
      </div>
      <div>
        <label htmlFor="brand" className="block text-sm font-medium text-slate-700">
          Marque
        </label>
        <select
          id="brand"
          value={selectedBrand ?? ''}
          onChange={(e) =>
            onChange({
              category: selectedCategory,
              brand: e.target.value || undefined,
              sort: selectedSort,
            })
          }
          className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm"
        >
          <option value="">Toutes</option>
          {brands.map((b) => (
            <option key={b.id} value={b.slug}>
              {b.name}
            </option>
          ))}
        </select>
      </div>
    </div>
  );
}
