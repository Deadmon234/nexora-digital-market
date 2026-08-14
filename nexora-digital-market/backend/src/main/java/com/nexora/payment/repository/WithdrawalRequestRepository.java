package com.nexora.payment.repository;

import com.nexora.common.enums.WithdrawalStatus;
import com.nexora.payment.entity.WithdrawalRequest;
import com.nexora.seller.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawalRequestRepository extends JpaRepository<WithdrawalRequest, Long> {
    List<WithdrawalRequest> findBySellerOrderByCreatedAtDesc(Seller seller);
    List<WithdrawalRequest> findAllByOrderByCreatedAtDesc();
    long countByStatus(WithdrawalStatus status);
}
