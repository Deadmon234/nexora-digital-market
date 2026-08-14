'use client';

import { Button } from '@/components/ui/Button';
import { RatingStars } from '@/components/review/RatingStars';
import { ApiError } from '@/services/api.service';
import {
  createProductReview,
  createShopReview,
  updateProductReview,
  updateShopReview,
} from '@/services/review.service';
import { FormEvent, useState } from 'react';

interface ReviewFormProps {
  targetType: 'product' | 'shop';
  slug: string;
  existingReviewId?: number;
  initialRating?: number;
  initialComment?: string;
  onSuccess: () => void;
}

export function ReviewForm({
  targetType,
  slug,
  existingReviewId,
  initialRating = 5,
  initialComment = '',
  onSuccess,
}: ReviewFormProps) {
  const [rating, setRating] = useState(initialRating);
  const [comment, setComment] = useState(initialComment);
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setSubmitting(true);
    setError('');
    try {
      const payload = { rating, comment: comment.trim() || undefined };
      if (existingReviewId) {
        if (targetType === 'product') {
          await updateProductReview(existingReviewId, payload);
        } else {
          await updateShopReview(existingReviewId, payload);
        }
      } else if (targetType === 'product') {
        await createProductReview(slug, payload);
      } else {
        await createShopReview(slug, payload);
      }
      onSuccess();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : 'Erreur lors de l\'envoi');
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="rounded-xl border border-slate-200 bg-slate-50 p-4 space-y-4">
      <h3 className="font-semibold text-slate-900">
        {existingReviewId ? 'Modifier mon avis' : 'Laisser un avis'}
      </h3>
      <div>
        <p className="mb-2 text-sm text-slate-600">Votre note</p>
        <RatingStars rating={rating} interactive onChange={setRating} />
      </div>
      <div>
        <label className="block text-sm text-slate-600">Commentaire (optionnel)</label>
        <textarea
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          rows={3}
          maxLength={2000}
          className="mt-1 w-full rounded-lg border border-slate-300 px-3 py-2 text-sm"
          placeholder="Partagez votre expérience..."
        />
      </div>
      {error && <p className="text-sm text-red-600">{error}</p>}
      <Button type="submit" disabled={submitting}>
        {submitting ? 'Envoi...' : existingReviewId ? 'Mettre à jour' : 'Publier'}
      </Button>
    </form>
  );
}
