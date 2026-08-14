package com.nexora.payment.repository;

import com.nexora.payment.entity.Commission;
import com.nexora.seller.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommissionRepository extends JpaRepository<Commission, Long> {
    List<Commission> findBySellerOrderByCreatedAtDesc(Seller seller);
    List<Commission> findAllByOrderByCreatedAtDesc();
}
