import { RatingStars } from '@/components/review/RatingStars';
import type { Review } from '@/types/review';

interface ReviewCardProps {
  review: Review;
  onDelete?: (id: number) => void;
}

export function ReviewCard({ review, onDelete }: ReviewCardProps) {
  return (
    <article className="rounded-xl border border-slate-200 bg-white p-4">
      <div className="flex items-start justify-between gap-4">
        <div>
          <p className="font-medium text-slate-900">{review.authorName}</p>
          <p className="text-xs text-slate-500">
            {new Date(review.createdAt).toLocaleDateString('fr-FR')}
          </p>
        </div>
        <RatingStars rating={review.rating} size="sm" />
      </div>
      {review.comment && <p className="mt-3 text-sm text-slate-700">{review.comment}</p>}
      {review.ownReview && onDelete && (
        <button
          type="button"
          onClick={() => onDelete(review.id)}
          className="mt-3 text-xs text-red-600 hover:underline"
        >
          Supprimer mon avis
        </button>
      )}
    </article>
  );
}
