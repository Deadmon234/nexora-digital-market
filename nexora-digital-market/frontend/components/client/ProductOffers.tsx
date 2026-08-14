'use client';

import type { ProductOffer } from '@/types/product';
import { addToCart } from '@/services/cart.service';
import { formatPrice } from '@/services/catalog.service';
import { useRouter } from 'next/navigation';
import { useState } from 'react';
import { Button } from '@/components/ui/Button';

interface ProductOffersProps {
  offers: ProductOffer[];
}

export function ProductOffers({ offers }: ProductOffersProps) {
  const router = useRouter();
  const [loadingId, setLoadingId] = useState<number | null>(null);
  const [message, setMessage] = useState('');

  async function handleAdd(offerId: number) {
    setLoadingId(offerId);
    setMessage('');
    try {
      await addToCart(offerId, 1);
      setMessage('Ajouté au panier');
    } catch (err) {
      setMessage(err instanceof Error ? err.message : 'Erreur');
    } finally {
      setLoadingId(null);
    }
  }

  if (offers.length === 0) {
    return <p className="mt-2 text-sm text-slate-600">Aucune offre disponible.</p>;
  }

  return (
    <div>
      <ul className="mt-4 space-y-3">
        {offers.map((offer) => (
          <li
            key={offer.id}
            className="flex flex-col gap-3 rounded-xl border border-slate-200 bg-white px-4 py-3 sm:flex-row sm:items-center sm:justify-between"
          >
            <div>
              <p className="font-medium text-slate-900">{offer.sellerName}</p>
              <p className="text-xs text-slate-500">
                {offer.conditionLabel ?? 'Standard'} · Stock : {offer.stock}
              </p>
            </div>
            <div className="flex items-center gap-3">
              <span className="text-lg font-bold">{formatPrice(offer.price)}</span>
              <Button
                type="button"
                disabled={loadingId === offer.id || offer.stock <= 0}
                onClick={() => handleAdd(offer.id)}
              >
                {offer.stock <= 0 ? 'Rupture' : loadingId === offer.id ? '...' : 'Ajouter au panier'}
              </Button>
            </div>
          </li>
        ))}
      </ul>
      {message && (
        <p className="mt-3 text-sm text-slate-600">
          {message}{' '}
          <button type="button" className="underline" onClick={() => router.push('/cart')}>
            Voir le panier
          </button>
        </p>
      )}
    </div>
  );
}
