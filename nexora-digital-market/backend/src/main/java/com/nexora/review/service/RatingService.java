package com.nexora.review.service;

import com.nexora.product.entity.Product;
import com.nexora.review.dto.RatingDto;
import com.nexora.review.repository.ProductReviewRepository;
import com.nexora.shop.entity.Shop;
import com.nexora.review.repository.ShopReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final ProductReviewRepository productReviewRepository;
    private final ShopReviewRepository shopReviewRepository;

    @Transactional(readOnly = true)
    public RatingDto getProductRating(Product product) {
        return RatingDto.builder()
                .averageRating(round(productReviewRepository.averageRatingByProduct(product)))
                .reviewCount(productReviewRepository.countByProduct(product))
                .build();
    }

    @Transactional(readOnly = true)
    public RatingDto getShopRating(Shop shop) {
        return RatingDto.builder()
                .averageRating(round(shopReviewRepository.averageRatingByShop(shop)))
                .reviewCount(shopReviewRepository.countByShop(shop))
                .build();
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
