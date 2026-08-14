package com.nexora.order.dto;

import com.nexora.common.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderSummaryDto {
    private Long id;
    private String orderNumber;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private int itemCount;
    private int sellerCount;
    private LocalDateTime createdAt;
}
