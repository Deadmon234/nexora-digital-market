package com.nexora.review.controller;

import com.nexora.review.dto.CreateReviewRequest;
import com.nexora.review.dto.ReviewDto;
import com.nexora.review.dto.ReviewSummaryDto;
import com.nexora.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Avis")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/products/{slug}")
    @Operation(summary = "Avis d'un produit")
    public ReviewSummaryDto getProductReviews(@PathVariable String slug) {
        return reviewService.getProductReviews(slug);
    }

    @PostMapping("/products/{slug}")
    @Operation(summary = "Laisser un avis produit")
    public ResponseEntity<ReviewDto> createProductReview(
            @PathVariable String slug,
            @Valid @RequestBody CreateReviewRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createProductReview(slug, request));
    }

    @PutMapping("/products/{id}")
    @Operation(summary = "Modifier mon avis produit")
    public ReviewDto updateProductReview(@PathVariable Long id, @Valid @RequestBody CreateReviewRequest request) {
        return reviewService.updateProductReview(id, request);
    }

    @DeleteMapping("/products/{id}")
    @Operation(summary = "Supprimer mon avis produit")
    public ResponseEntity<Void> deleteProductReview(@PathVariable Long id) {
        reviewService.deleteProductReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/shops/{slug}")
    @Operation(summary = "Avis d'une boutique")
    public ReviewSummaryDto getShopReviews(@PathVariable String slug) {
        return reviewService.getShopReviews(slug);
    }

    @PostMapping("/shops/{slug}")
    @Operation(summary = "Laisser un avis boutique")
    public ResponseEntity<ReviewDto> createShopReview(
            @PathVariable String slug,
            @Valid @RequestBody CreateReviewRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createShopReview(slug, request));
    }

    @PutMapping("/shops/{id}")
    @Operation(summary = "Modifier mon avis boutique")
    public ReviewDto updateShopReview(@PathVariable Long id, @Valid @RequestBody CreateReviewRequest request) {
        return reviewService.updateShopReview(id, request);
    }

    @DeleteMapping("/shops/{id}")
    @Operation(summary = "Supprimer mon avis boutique")
    public ResponseEntity<Void> deleteShopReview(@PathVariable Long id) {
        reviewService.deleteShopReview(id);
        return ResponseEntity.noContent().build();
    }
}
