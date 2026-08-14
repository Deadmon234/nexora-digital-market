package com.nexora.admin.dto;

import com.nexora.common.enums.WithdrawalStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class AdminWithdrawalDto {
    private Long id;
    private Long sellerId;
    private String sellerCompanyName;
    private BigDecimal amount;
    private String bankAccount;
    private WithdrawalStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
}
