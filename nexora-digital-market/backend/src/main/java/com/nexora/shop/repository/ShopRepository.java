package com.nexora.shop.repository;

import com.nexora.shop.entity.Shop;
import com.nexora.seller.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findBySlug(String slug);
    Optional<Shop> findBySlugAndActiveTrue(String slug);
    List<Shop> findBySeller(Seller seller);
    boolean existsBySlug(String slug);
    List<Shop> findAllByOrderByCreatedAtDesc();
}
