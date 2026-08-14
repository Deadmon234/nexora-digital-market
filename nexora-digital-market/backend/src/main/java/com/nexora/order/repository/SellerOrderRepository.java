package com.nexora.order.repository;

import com.nexora.order.entity.SellerOrder;
import com.nexora.seller.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SellerOrderRepository extends JpaRepository<SellerOrder, Long> {
    List<SellerOrder> findBySellerOrderByCreatedAtDesc(Seller seller);
    Optional<SellerOrder> findByIdAndSeller(Long id, Seller seller);
    List<SellerOrder> findByOrderId(Long orderId);
}
