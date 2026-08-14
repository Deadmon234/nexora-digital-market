package com.nexora.product.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class ProductDetailDto {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private CategoryDto category;
    private BrandDto brand;
    private List<ProductImageDto> images;
    private List<ProductOfferDto> offers;
    private BigDecimal minPrice;
}
