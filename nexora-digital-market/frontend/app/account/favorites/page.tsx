'use client';

import { formatPrice } from '@/services/catalog.service';
import { getFavorites, removeFavorite } from '@/services/favorite.service';
import type { Favorite } from '@/types/client';
import Link from 'next/link';
import { useEffect, useState } from 'react';
import { Button } from '@/components/ui/Button';

export default function AccountFavoritesPage() {
  const [favorites, setFavorites] = useState<Favorite[]>([]);
  const [loading, setLoading] = useState(true);

  async function load() {
    setLoading(true);
    try {
      setFavorites(await getFavorites());
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  if (loading) {
    return <p className="text-slate-600">Chargement...</p>;
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900">Mes favoris</h1>
      {favorites.length === 0 ? (
        <p className="text-slate-600">Aucun favori pour le moment.</p>
      ) : (
        <ul className="grid gap-4 sm:grid-cols-2">
          {favorites.map((fav) => (
            <li key={fav.id} className="rounded-xl border border-slate-200 bg-white p-4">
              {fav.imageUrl && (
                // eslint-disable-next-line @next/next/no-img-element
                <img src={fav.imageUrl} alt={fav.productName} className="mb-3 h-32 w-full rounded-lg object-cover" />
              )}
              <Link href={`/products/${fav.productSlug}`} className="font-medium hover:underline">
                {fav.productName}
              </Link>
              {fav.minPrice != null && (
                <p className="mt-1 text-sm font-semibold">{formatPrice(fav.minPrice)}</p>
              )}
              <Button
                type="button"
                variant="outline"
                className="mt-3"
                onClick={() => removeFavorite(fav.productId).then(load)}
              >
                Retirer
              </Button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
