package com.nexora.seller.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class SellerProductDto {
    private Long offerId;
    private Long productId;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private Integer stock;
    private String conditionLabel;
    private boolean active;
}
