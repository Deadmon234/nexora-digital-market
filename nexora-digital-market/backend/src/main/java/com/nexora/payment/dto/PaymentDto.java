package com.nexora.payment.dto;

import com.nexora.common.enums.PaymentMethod;
import com.nexora.common.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentDto {
    private Long id;
    private Long orderId;
    private String orderNumber;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private String transactionRef;
    private LocalDateTime createdAt;
}
