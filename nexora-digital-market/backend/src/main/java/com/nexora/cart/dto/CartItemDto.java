package com.nexora.cart.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CartItemDto {
    private Long id;
    private Long offerId;
    private Long productId;
    private String productName;
    private String productSlug;
    private String sellerName;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
    private Integer availableStock;
}
