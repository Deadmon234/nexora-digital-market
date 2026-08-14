package com.nexora.seller.repository;

import com.nexora.common.enums.SellerStatus;
import com.nexora.seller.entity.Seller;
import com.nexora.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByUser(User user);
    boolean existsByUser(User user);
    List<Seller> findAllByOrderByCreatedAtDesc();
    long countByStatus(SellerStatus status);
}
