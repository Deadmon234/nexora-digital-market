package com.nexora.review.repository;

import com.nexora.review.entity.ShopReview;
import com.nexora.shop.entity.Shop;
import com.nexora.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShopReviewRepository extends JpaRepository<ShopReview, Long> {
    List<ShopReview> findByShopOrderByCreatedAtDesc(Shop shop);

    Optional<ShopReview> findByIdAndUser(Long id, User user);

    boolean existsByShopAndUser(Shop shop, User user);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM ShopReview r WHERE r.shop = :shop")
    double averageRatingByShop(@Param("shop") Shop shop);

    long countByShop(Shop shop);
}
