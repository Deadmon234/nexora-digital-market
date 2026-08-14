package com.nexora.product.repository;

import com.nexora.product.entity.Product;
import com.nexora.product.entity.ProductOffer;
import com.nexora.seller.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductOfferRepository extends JpaRepository<ProductOffer, Long> {
    List<ProductOffer> findByProductAndActiveTrueOrderByPriceAsc(Product product);
    List<ProductOffer> findBySellerOrderByCreatedAtDesc(Seller seller);
    Optional<ProductOffer> findByIdAndSeller(Long id, Seller seller);
    long countBySellerAndActiveTrue(Seller seller);
}
