'use client';

import { addFavorite, checkFavorite, removeFavorite } from '@/services/favorite.service';
import { useEffect, useState } from 'react';
import { Button } from '@/components/ui/Button';

interface FavoriteButtonProps {
  productId: number;
}

export function FavoriteButton({ productId }: FavoriteButtonProps) {
  const [favorited, setFavorited] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    checkFavorite(productId)
      .then((res) => setFavorited(res.favorited))
      .catch(() => setFavorited(false))
      .finally(() => setLoading(false));
  }, [productId]);

  async function toggle() {
    setLoading(true);
    try {
      if (favorited) {
        await removeFavorite(productId);
        setFavorited(false);
      } else {
        await addFavorite(productId);
        setFavorited(true);
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <Button type="button" variant="outline" disabled={loading} onClick={toggle}>
      {favorited ? 'Retirer des favoris' : 'Ajouter aux favoris'}
    </Button>
  );
}
