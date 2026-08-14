'use client';

import { ProductForm } from '@/components/seller/ProductForm';
import { createSellerProduct } from '@/services/seller.service';
import { useRouter } from 'next/navigation';

export default function NewSellerProductPage() {
  const router = useRouter();

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">Nouveau produit</h1>
      <ProductForm
        submitLabel="Créer le produit"
        onSubmit={async (data) => {
          await createSellerProduct(data);
          router.push('/seller/products');
        }}
      />
    </div>
  );
}
