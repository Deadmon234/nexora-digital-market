'use client';

import { Button } from '@/components/ui/Button';
import { deleteSellerProduct, getSellerProducts } from '@/services/seller.service';
import type { SellerProduct } from '@/types/seller';
import Link from 'next/link';
import { useEffect, useState } from 'react';

export default function SellerProductsPage() {
  const [products, setProducts] = useState<SellerProduct[]>([]);
  const [loading, setLoading] = useState(true);

  async function load() {
    setLoading(true);
    try {
      setProducts(await getSellerProducts());
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function handleDelete(offerId: number) {
    if (!confirm('Désactiver ce produit ?')) return;
    await deleteSellerProduct(offerId);
    await load();
  }

  if (loading) {
    return <p className="text-slate-600">Chargement...</p>;
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-900">Mes produits</h1>
        <Link href="/seller/products/new">
          <Button>Nouveau produit</Button>
        </Link>
      </div>

      {products.length === 0 ? (
        <p className="text-slate-600">Aucun produit pour le moment.</p>
      ) : (
        <div className="overflow-x-auto rounded-2xl border border-slate-200 bg-white">
          <table className="min-w-full text-sm">
            <thead className="border-b border-slate-200 bg-slate-50 text-left">
              <tr>
                <th className="px-4 py-3 font-medium">Produit</th>
                <th className="px-4 py-3 font-medium">Prix</th>
                <th className="px-4 py-3 font-medium">Stock</th>
                <th className="px-4 py-3 font-medium">Statut</th>
                <th className="px-4 py-3 font-medium">Actions</th>
              </tr>
            </thead>
            <tbody>
              {products.map((product) => (
                <tr key={product.offerId} className="border-b border-slate-100">
                  <td className="px-4 py-3">{product.name}</td>
                  <td className="px-4 py-3">{product.price.toFixed(2)} €</td>
                  <td className="px-4 py-3">{product.stock}</td>
                  <td className="px-4 py-3">{product.active ? 'Actif' : 'Inactif'}</td>
                  <td className="px-4 py-3">
                    <div className="flex gap-2">
                      <Link
                        href={`/seller/products/${product.offerId}/edit`}
                        className="text-slate-900 underline"
                      >
                        Modifier
                      </Link>
                      <button
                        type="button"
                        onClick={() => handleDelete(product.offerId)}
                        className="text-red-600 underline"
                      >
                        Supprimer
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
