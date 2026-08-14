import { MainLayout } from '@/components/layout/MainLayout';
import { FavoriteButton } from '@/components/client/FavoriteButton';
import { ProductOffers } from '@/components/client/ProductOffers';
import { ProductReviews } from '@/components/review/ProductReviews';
import { formatPrice, getProduct } from '@/services/catalog.service';
import Link from 'next/link';
import { notFound } from 'next/navigation';

interface Props {
  params: { slug: string };
}

export const dynamic = 'force-dynamic';

export default async function ProductDetailPage({ params }: Props) {
  let product;
  try {
    product = await getProduct(params.slug);
  } catch {
    notFound();
  }

  const primaryImage = product.images.find((i) => i.primary) ?? product.images[0];

  return (
    <MainLayout>
      <div className="mx-auto max-w-6xl px-4 py-10">
        <nav className="mb-6 text-sm text-slate-500">
          <Link href="/products" className="hover:text-slate-900">
            Catalogue
          </Link>
          {product.category && (
            <>
              {' / '}
              <Link href={`/categories/${product.category.slug}`} className="hover:text-slate-900">
                {product.category.name}
              </Link>
            </>
          )}
          {' / '}
          <span className="text-slate-900">{product.name}</span>
        </nav>

        <div className="grid gap-10 lg:grid-cols-2">
          <div className="overflow-hidden rounded-2xl border border-slate-200 bg-white">
            {primaryImage ? (
              // eslint-disable-next-line @next/next/no-img-element
              <img src={primaryImage.url} alt={primaryImage.altText ?? product.name} className="w-full object-cover" />
            ) : (
              <div className="flex aspect-square items-center justify-center bg-slate-100 text-slate-400">
                Sans image
              </div>
            )}
          </div>

          <div>
            {product.brand && (
              <p className="text-sm font-medium uppercase tracking-wide text-slate-500">{product.brand.name}</p>
            )}
            <h1 className="mt-2 text-3xl font-bold text-slate-900">{product.name}</h1>
            <div className="mt-4 flex items-center gap-4">
              <p className="text-2xl font-bold text-slate-900">
                À partir de {formatPrice(product.minPrice)}
              </p>
              <FavoriteButton productId={product.id} />
            </div>
            <p className="mt-6 text-slate-700">{product.description}</p>

            <div className="mt-8">
              <h2 className="text-lg font-semibold text-slate-900">Offres vendeurs</h2>
              <ProductOffers offers={product.offers} />
            </div>
          </div>
        </div>

        <section className="mt-12">
          <h2 className="mb-6 text-xl font-bold text-slate-900">Avis clients</h2>
          <ProductReviews slug={params.slug} />
        </section>
      </div>
    </MainLayout>
  );
}
