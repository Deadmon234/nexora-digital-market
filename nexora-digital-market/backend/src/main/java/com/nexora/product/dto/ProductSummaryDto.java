package com.nexora.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductSummaryDto {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private String categoryName;
    private String categorySlug;
    private String brandName;
    private String brandSlug;
    private String imageUrl;
    private BigDecimal minPrice;
    private Integer offerCount;
}
