import { ProductCard } from '@/components/product/ProductCard';
import type { ProductSummary } from '@/types/product';

interface ProductGridProps {
  products: ProductSummary[];
  emptyMessage?: string;
}

export function ProductGrid({ products, emptyMessage = 'Aucun produit trouvé.' }: ProductGridProps) {
  if (products.length === 0) {
    return (
      <div className="rounded-2xl border border-dashed border-slate-300 bg-white p-12 text-center text-slate-600">
        {emptyMessage}
      </div>
    );
  }

  return (
    <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
      {products.map((product) => (
        <ProductCard key={product.id} product={product} />
      ))}
    </div>
  );
}
