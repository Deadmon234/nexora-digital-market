interface RatingStarsProps {
  rating: number;
  max?: number;
  size?: 'sm' | 'md';
  interactive?: boolean;
  onChange?: (rating: number) => void;
}

export function RatingStars({
  rating,
  max = 5,
  size = 'md',
  interactive = false,
  onChange,
}: RatingStarsProps) {
  const starSize = size === 'sm' ? 'text-base' : 'text-xl';

  return (
    <div className="flex items-center gap-0.5" aria-label={`Note : ${rating} sur ${max}`}>
      {Array.from({ length: max }, (_, i) => {
        const value = i + 1;
        const filled = value <= Math.round(rating);
        return (
          <button
            key={value}
            type="button"
            disabled={!interactive}
            onClick={() => interactive && onChange?.(value)}
            className={`${starSize} ${interactive ? 'cursor-pointer hover:scale-110' : 'cursor-default'} ${
              filled ? 'text-amber-400' : 'text-slate-300'
            }`}
            aria-label={`${value} étoile${value > 1 ? 's' : ''}`}
          >
            ★
          </button>
        );
      })}
    </div>
  );
}
