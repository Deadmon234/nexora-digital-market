import { MainLayout } from '@/components/layout/MainLayout';
import { ShopReviews } from '@/components/review/ShopReviews';
import { getShop } from '@/services/review.service';
import { notFound } from 'next/navigation';

interface Props {
  params: { slug: string };
}

export const dynamic = 'force-dynamic';

export default async function ShopPage({ params }: Props) {
  let shop;
  try {
    shop = await getShop(params.slug);
  } catch {
    notFound();
  }

  return (
    <MainLayout>
      <div className="mx-auto max-w-4xl px-4 py-10">
        <h1 className="text-3xl font-bold text-slate-900">{shop.name}</h1>
        {shop.description && <p className="mt-4 text-slate-700">{shop.description}</p>}

        <section className="mt-10">
          <h2 className="mb-6 text-xl font-bold text-slate-900">Avis sur la boutique</h2>
          <ShopReviews slug={params.slug} />
        </section>
      </div>
    </MainLayout>
  );
}
