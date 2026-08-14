package com.nexora.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RatingDto {
    private double averageRating;
    private long reviewCount;
}
