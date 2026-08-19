import Link from 'next/link';
import { formatPrice } from '@/services/catalog.service';
import type { ProductSummary } from '@/types/product';

interface ProductCardProps {
  product: ProductSummary;
}

export function ProductCard({ product }: ProductCardProps) {
  return (
    <Link
      href={`/products/${product.slug}`}
      className="group flex flex-col overflow-hidden rounded-xl border border-slate-200/80 bg-white shadow-sm transition duration-300 hover:-translate-y-1 hover:border-nexora-coral/40 hover:shadow-xl"
    >
      <div className="aspect-[4/3] overflow-hidden bg-[#ece9e2]">
        {product.imageUrl ? (
          // eslint-disable-next-line @next/next/no-img-element
          <img
            src={product.imageUrl}
            alt={product.name}
            className="h-full w-full object-cover transition duration-500 group-hover:scale-105"
          />
        ) : (
          <div className="flex h-full items-center justify-center text-slate-400">Sans image</div>
        )}
      </div>
      <div className="flex flex-1 flex-col p-4">
        {product.brandName && (
          <p className="text-xs font-bold uppercase tracking-widest text-nexora-coral">{product.brandName}</p>
        )}
        <h3 className="mt-1 font-semibold leading-6 text-slate-900 group-hover:text-nexora-blue">{product.name}</h3>
        <p className="mt-2 line-clamp-2 flex-1 text-sm text-slate-600">{product.description}</p>
        <div className="mt-4 flex items-center justify-between">
          <span className="text-lg font-bold text-nexora-navy">{formatPrice(product.minPrice)}</span>
          {product.offerCount != null && product.offerCount > 0 && (
            <span className="text-xs text-slate-500">{product.offerCount} offre(s)</span>
          )}
        </div>
      </div>
    </Link>
  );
}
