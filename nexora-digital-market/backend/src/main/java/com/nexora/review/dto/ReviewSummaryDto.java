package com.nexora.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewSummaryDto {
    private double averageRating;
    private long reviewCount;
    private List<ReviewDto> reviews;
}
