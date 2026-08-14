package com.nexora.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductOfferDto {

    private Long id;
    private Long sellerId;
    private String sellerName;
    private BigDecimal price;
    private Integer stock;
    private String conditionLabel;
}
