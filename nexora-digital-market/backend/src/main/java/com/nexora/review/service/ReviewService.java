package com.nexora.review.service;

import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.common.exception.ValidationException;
import com.nexora.common.util.InputSanitizer;
import com.nexora.order.repository.OrderItemRepository;
import com.nexora.product.entity.Product;
import com.nexora.product.repository.ProductRepository;
import com.nexora.review.dto.CreateReviewRequest;
import com.nexora.review.dto.ReviewDto;
import com.nexora.review.dto.ReviewSummaryDto;
import com.nexora.review.entity.ProductReview;
import com.nexora.review.entity.Review;
import com.nexora.review.entity.ShopReview;
import com.nexora.review.repository.ProductReviewRepository;
import com.nexora.review.repository.ShopReviewRepository;
import com.nexora.shop.entity.Shop;
import com.nexora.shop.repository.ShopRepository;
import com.nexora.user.entity.User;
import com.nexora.user.security.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ProductReviewRepository productReviewRepository;
    private final ShopReviewRepository shopReviewRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserContextService userContextService;
    private final RatingService ratingService;

    @Transactional(readOnly = true)
    public ReviewSummaryDto getProductReviews(String slug) {
        Product product = findActiveProduct(slug);
        Optional<User> currentUser = userContextService.getCurrentUserOptional();
        List<ReviewDto> reviews = productReviewRepository.findByProductOrderByCreatedAtDesc(product).stream()
                .map(r -> toDto(r, currentUser))
                .toList();
        var ratingDto = ratingService.getProductRating(product);
        return ReviewSummaryDto.builder()
                .averageRating(ratingDto.getAverageRating())
                .reviewCount(ratingDto.getReviewCount())
                .reviews(reviews)
                .build();
    }

    @Transactional
    public ReviewDto createProductReview(String slug, CreateReviewRequest request) {
        User user = userContextService.getCurrentUser();
        Product product = findActiveProduct(slug);

        if (!orderItemRepository.existsPurchasedByUserAndProductSlug(user, slug)) {
            throw new ValidationException("Vous devez avoir acheté ce produit pour laisser un avis");
        }
        if (productReviewRepository.existsByProductAndUser(product, user)) {
            throw new ValidationException("Vous avez déjà laissé un avis pour ce produit");
        }

        ProductReview review = ProductReview.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .comment(InputSanitizer.sanitizeText(request.getComment()))
                .build();

        return toDto(productReviewRepository.save(review), Optional.of(user));
    }

    @Transactional
    public ReviewDto updateProductReview(Long id, CreateReviewRequest request) {
        User user = userContextService.getCurrentUser();
        ProductReview review = productReviewRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Avis introuvable"));
        review.setRating(request.getRating());
        review.setComment(InputSanitizer.sanitizeText(request.getComment()));
        return toDto(productReviewRepository.save(review), Optional.of(user));
    }

    @Transactional
    public void deleteProductReview(Long id) {
        User user = userContextService.getCurrentUser();
        ProductReview review = productReviewRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Avis introuvable"));
        productReviewRepository.delete(review);
    }

    @Transactional(readOnly = true)
    public ReviewSummaryDto getShopReviews(String slug) {
        Shop shop = findActiveShop(slug);
        Optional<User> currentUser = userContextService.getCurrentUserOptional();
        List<ReviewDto> reviews = shopReviewRepository.findByShopOrderByCreatedAtDesc(shop).stream()
                .map(r -> toDto(r, currentUser))
                .toList();
        var ratingDto = ratingService.getShopRating(shop);
        return ReviewSummaryDto.builder()
                .averageRating(ratingDto.getAverageRating())
                .reviewCount(ratingDto.getReviewCount())
                .reviews(reviews)
                .build();
    }

    @Transactional
    public ReviewDto createShopReview(String slug, CreateReviewRequest request) {
        User user = userContextService.getCurrentUser();
        Shop shop = findActiveShop(slug);

        if (!orderItemRepository.existsPurchasedFromSeller(user, shop.getSeller().getId())) {
            throw new ValidationException("Vous devez avoir commandé chez ce vendeur pour laisser un avis");
        }
        if (shopReviewRepository.existsByShopAndUser(shop, user)) {
            throw new ValidationException("Vous avez déjà laissé un avis pour cette boutique");
        }

        ShopReview review = ShopReview.builder()
                .user(user)
                .shop(shop)
                .rating(request.getRating())
                .comment(InputSanitizer.sanitizeText(request.getComment()))
                .build();

        return toDto(shopReviewRepository.save(review), Optional.of(user));
    }

    @Transactional
    public ReviewDto updateShopReview(Long id, CreateReviewRequest request) {
        User user = userContextService.getCurrentUser();
        ShopReview review = shopReviewRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Avis introuvable"));
        review.setRating(request.getRating());
        review.setComment(InputSanitizer.sanitizeText(request.getComment()));
        return toDto(shopReviewRepository.save(review), Optional.of(user));
    }

    @Transactional
    public void deleteShopReview(Long id) {
        User user = userContextService.getCurrentUser();
        ShopReview review = shopReviewRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Avis introuvable"));
        shopReviewRepository.delete(review);
    }

    private Product findActiveProduct(String slug) {
        return productRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable : " + slug));
    }

    private Shop findActiveShop(String slug) {
        return shopRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Boutique introuvable : " + slug));
    }

    private ReviewDto toDto(Review review, Optional<User> currentUser) {
        User author = review.getUser();
        String authorName = author.getFirstName() + " " + author.getLastName().charAt(0) + ".";
        boolean ownReview = currentUser.map(u -> u.getId().equals(author.getId())).orElse(false);
        return ReviewDto.builder()
                .id(review.getId())
                .authorName(authorName)
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .ownReview(ownReview)
                .build();
    }
}
