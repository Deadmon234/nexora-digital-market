package com.nexora.admin.service;

import com.nexora.admin.dto.AdminCommissionDto;
import com.nexora.payment.entity.Commission;
import com.nexora.payment.repository.CommissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCommissionService {

    private final CommissionRepository commissionRepository;

    @Transactional(readOnly = true)
    public List<AdminCommissionDto> findAll() {
        return commissionRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .toList();
    }

    private AdminCommissionDto toDto(Commission commission) {
        return AdminCommissionDto.builder()
                .id(commission.getId())
                .sellerId(commission.getSeller().getId())
                .sellerCompanyName(commission.getSeller().getCompanyName())
                .orderNumber(commission.getSellerOrder().getOrder().getOrderNumber())
                .orderAmount(commission.getOrderAmount())
                .commissionRate(commission.getCommissionRate())
                .commissionAmount(commission.getCommissionAmount())
                .sellerAmount(commission.getSellerAmount())
                .createdAt(commission.getCreatedAt())
                .build();
    }
}
