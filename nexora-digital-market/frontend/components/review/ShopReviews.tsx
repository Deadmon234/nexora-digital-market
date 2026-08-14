'use client';

import { ReviewForm } from '@/components/review/ReviewForm';
import { ReviewList } from '@/components/review/ReviewList';
import { deleteShopReview, getShopReviews } from '@/services/review.service';
import type { ReviewSummary } from '@/types/review';
import { useCallback, useEffect, useState } from 'react';

interface ShopReviewsProps {
  slug: string;
}

export function ShopReviews({ slug }: ShopReviewsProps) {
  const [summary, setSummary] = useState<ReviewSummary | null>(null);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);

  const load = useCallback(async () => {
    setLoading(true);
    try {
      setSummary(await getShopReviews(slug));
    } finally {
      setLoading(false);
    }
  }, [slug]);

  useEffect(() => {
    load();
  }, [load]);

  const ownReview = summary?.reviews.find((r) => r.ownReview);

  async function handleDelete(id: number) {
    await deleteShopReview(id);
    load();
  }

  if (loading) {
    return <p className="text-sm text-slate-500">Chargement des avis...</p>;
  }

  if (!summary) {
    return null;
  }

  return (
    <div className="space-y-6">
      <ReviewList summary={summary} onDelete={handleDelete} />
      {!ownReview && !showForm && (
        <button
          type="button"
          onClick={() => setShowForm(true)}
          className="text-sm font-medium text-indigo-600 hover:underline"
        >
          Laisser un avis sur la boutique
        </button>
      )}
      {(showForm || ownReview) && (
        <ReviewForm
          targetType="shop"
          slug={slug}
          existingReviewId={ownReview?.id}
          initialRating={ownReview?.rating ?? 5}
          initialComment={ownReview?.comment ?? ''}
          onSuccess={() => {
            setShowForm(false);
            load();
          }}
        />
      )}
    </div>
  );
}
