package com.nexora.admin.service;

import com.nexora.admin.dto.AdminWithdrawalDto;
import com.nexora.admin.dto.UpdateWithdrawalStatusRequest;
import com.nexora.common.enums.WithdrawalStatus;
import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.common.exception.ValidationException;
import com.nexora.payment.entity.WithdrawalRequest;
import com.nexora.payment.repository.WithdrawalRequestRepository;
import com.nexora.payment.service.SellerBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminWithdrawalService {

    private final WithdrawalRequestRepository withdrawalRepository;
    private final SellerBalanceService sellerBalanceService;

    @Transactional(readOnly = true)
    public List<AdminWithdrawalDto> findAll() {
        return withdrawalRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public AdminWithdrawalDto updateStatus(Long id, UpdateWithdrawalStatusRequest request) {
        WithdrawalRequest withdrawal = withdrawalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande de retrait introuvable"));

        if (withdrawal.getStatus() != WithdrawalStatus.PENDING) {
            throw new ValidationException("Cette demande a déjà été traitée");
        }

        WithdrawalStatus next = request.getStatus();
        if (next == WithdrawalStatus.PENDING) {
            throw new ValidationException("Statut invalide");
        }

        if (next == WithdrawalStatus.REJECTED) {
            sellerBalanceService.refundWithdrawal(withdrawal.getSeller(), withdrawal.getAmount());
            withdrawal.setStatus(WithdrawalStatus.REJECTED);
        } else if (next == WithdrawalStatus.PAID || next == WithdrawalStatus.APPROVED) {
            withdrawal.setStatus(WithdrawalStatus.PAID);
            withdrawal.setProcessedAt(LocalDateTime.now());
        } else {
            throw new ValidationException("Statut de retrait non supporté : " + next);
        }

        return toDto(withdrawalRepository.save(withdrawal));
    }

    private AdminWithdrawalDto toDto(WithdrawalRequest withdrawal) {
        return AdminWithdrawalDto.builder()
                .id(withdrawal.getId())
                .sellerId(withdrawal.getSeller().getId())
                .sellerCompanyName(withdrawal.getSeller().getCompanyName())
                .amount(withdrawal.getAmount())
                .bankAccount(withdrawal.getBankAccount())
                .status(withdrawal.getStatus())
                .createdAt(withdrawal.getCreatedAt())
                .processedAt(withdrawal.getProcessedAt())
                .build();
    }
}
