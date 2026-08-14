import { ReviewCard } from '@/components/review/ReviewCard';
import { RatingStars } from '@/components/review/RatingStars';
import type { ReviewSummary } from '@/types/review';

interface ReviewListProps {
  summary: ReviewSummary;
  onDelete?: (id: number) => void;
}

export function ReviewList({ summary, onDelete }: ReviewListProps) {
  return (
    <div className="space-y-4">
      <div className="flex items-center gap-4">
        <RatingStars rating={summary.averageRating} />
        <p className="text-sm text-slate-600">
          {summary.averageRating.toFixed(1)} / 5 — {summary.reviewCount} avis
        </p>
      </div>
      {summary.reviews.length === 0 ? (
        <p className="text-sm text-slate-500">Aucun avis pour le moment.</p>
      ) : (
        <div className="space-y-3">
          {summary.reviews.map((review) => (
            <ReviewCard key={review.id} review={review} onDelete={onDelete} />
          ))}
        </div>
      )}
    </div>
  );
}
