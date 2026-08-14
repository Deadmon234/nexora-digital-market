package com.nexora.payment.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class CommissionDto {
    private Long id;
    private Long sellerOrderId;
    private String orderNumber;
    private BigDecimal orderAmount;
    private BigDecimal commissionRate;
    private BigDecimal commissionAmount;
    private BigDecimal sellerAmount;
    private LocalDateTime createdAt;
}
