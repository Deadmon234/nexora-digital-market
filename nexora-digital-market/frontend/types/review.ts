export interface Review {
  id: number;
  authorName: string;
  rating: number;
  comment?: string;
  createdAt: string;
  ownReview: boolean;
}

export interface ReviewSummary {
  averageRating: number;
  reviewCount: number;
  reviews: Review[];
}

export interface CreateReviewPayload {
  rating: number;
  comment?: string;
}
