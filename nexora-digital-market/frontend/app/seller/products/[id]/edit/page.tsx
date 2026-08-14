'use client';

import { ProductForm } from '@/components/seller/ProductForm';
import { getSellerProduct, updateSellerProduct } from '@/services/seller.service';
import type { SellerProductPayload } from '@/types/seller';
import { useParams, useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';

export default function EditSellerProductPage() {
  const params = useParams<{ id: string }>();
  const router = useRouter();
  const offerId = Number(params.id);
  const [initial, setInitial] = useState<Partial<SellerProductPayload>>();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      const product = await getSellerProduct(offerId);
      setInitial({
        name: product.name,
        description: product.description,
        price: product.price,
        stock: product.stock,
        conditionLabel: product.conditionLabel,
        imageUrl: product.imageUrl,
      });
      setLoading(false);
    }
    load();
  }, [offerId]);

  if (loading) {
    return <p className="text-slate-600">Chargement...</p>;
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">Modifier le produit</h1>
      <ProductForm
        initial={initial}
        onSubmit={async (data) => {
          await updateSellerProduct(offerId, data);
          router.push('/seller/products');
        }}
      />
    </div>
  );
}
