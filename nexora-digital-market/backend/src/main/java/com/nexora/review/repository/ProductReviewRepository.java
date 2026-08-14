package com.nexora.review.repository;

import com.nexora.product.entity.Product;
import com.nexora.review.entity.ProductReview;
import com.nexora.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    List<ProductReview> findByProductOrderByCreatedAtDesc(Product product);

    Optional<ProductReview> findByIdAndUser(Long id, User user);

    boolean existsByProductAndUser(Product product, User user);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM ProductReview r WHERE r.product = :product")
    double averageRatingByProduct(@Param("product") Product product);

    long countByProduct(Product product);
}
