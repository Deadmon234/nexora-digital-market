package com.nexora.cart.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class CartDto {
    private Long id;
    private List<CartItemDto> items;
    private int itemCount;
    private BigDecimal totalAmount;
}
