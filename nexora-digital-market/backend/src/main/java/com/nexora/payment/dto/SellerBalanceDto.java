package com.nexora.payment.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SellerBalanceDto {
    private BigDecimal availableBalance;
    private BigDecimal totalEarned;
    private BigDecimal totalWithdrawn;
}
