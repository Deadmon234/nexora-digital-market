package com.nexora.payment.repository;

import com.nexora.payment.entity.SellerBalance;
import com.nexora.seller.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerBalanceRepository extends JpaRepository<SellerBalance, Long> {
    Optional<SellerBalance> findBySeller(Seller seller);
}
