package com.nexora.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewDto {
    private Long id;
    private String authorName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
    private boolean ownReview;
}
