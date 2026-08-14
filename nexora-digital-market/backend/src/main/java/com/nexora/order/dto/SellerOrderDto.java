package com.nexora.order.dto;

import com.nexora.common.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SellerOrderDto {
    private Long id;
    private String sellerName;
    private OrderStatus status;
    private BigDecimal subtotal;
    private int itemCount;
    private List<OrderItemDto> items;
    private LocalDateTime createdAt;
}
