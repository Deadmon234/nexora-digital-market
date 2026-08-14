package com.nexora.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class AdminCommissionDto {
    private Long id;
    private Long sellerId;
    private String sellerCompanyName;
    private String orderNumber;
    private BigDecimal orderAmount;
    private BigDecimal commissionRate;
    private BigDecimal commissionAmount;
    private BigDecimal sellerAmount;
    private LocalDateTime createdAt;
}
