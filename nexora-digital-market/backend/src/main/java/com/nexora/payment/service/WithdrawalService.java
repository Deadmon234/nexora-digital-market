package com.nexora.payment.service;

import com.nexora.common.enums.WithdrawalStatus;
import com.nexora.common.exception.ValidationException;
import com.nexora.payment.dto.WithdrawalDto;
import com.nexora.payment.dto.WithdrawalRequestDto;
import com.nexora.payment.entity.WithdrawalRequest;
import com.nexora.payment.repository.WithdrawalRequestRepository;
import com.nexora.seller.entity.Seller;
import com.nexora.seller.security.SellerContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WithdrawalService {

    private static final BigDecimal MIN_WITHDRAWAL = new BigDecimal("10.00");

    private final WithdrawalRequestRepository withdrawalRepository;
    private final SellerContextService sellerContextService;
    private final SellerBalanceService sellerBalanceService;

    @Transactional(readOnly = true)
    public List<WithdrawalDto> getMyWithdrawals() {
        Seller seller = sellerContextService.requireApprovedSeller();
        return withdrawalRepository.findBySellerOrderByCreatedAtDesc(seller).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public WithdrawalDto requestWithdrawal(WithdrawalRequestDto request) {
        Seller seller = sellerContextService.requireApprovedSeller();

        if (request.getAmount().compareTo(MIN_WITHDRAWAL) < 0) {
            throw new ValidationException("Montant minimum de retrait : " + MIN_WITHDRAWAL + " €");
        }

        sellerBalanceService.debitForWithdrawal(seller, request.getAmount());

        WithdrawalRequest withdrawal = WithdrawalRequest.builder()
                .seller(seller)
                .amount(request.getAmount())
                .bankAccount(request.getBankAccount())
                .status(WithdrawalStatus.PENDING)
                .build();

        return toDto(withdrawalRepository.save(withdrawal));
    }

    private WithdrawalDto toDto(WithdrawalRequest withdrawal) {
        return WithdrawalDto.builder()
                .id(withdrawal.getId())
                .amount(withdrawal.getAmount())
                .bankAccount(withdrawal.getBankAccount())
                .status(withdrawal.getStatus())
                .createdAt(withdrawal.getCreatedAt())
                .processedAt(withdrawal.getProcessedAt())
                .build();
    }
}
