package com.nexora.product.mapper;

import com.nexora.product.dto.*;
import com.nexora.product.entity.*;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static CategoryDto toCategoryDto(Category category, boolean includeChildren) {
        if (category == null) {
            return null;
        }
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .active(category.isActive())
                .children(includeChildren && category.getChildren() != null
                        ? category.getChildren().stream()
                        .filter(Category::isActive)
                        .map(c -> toCategoryDto(c, false))
                        .toList()
                        : List.of())
                .build();
    }

    public static BrandDto toBrandDto(Brand brand) {
        if (brand == null) {
            return null;
        }
        return BrandDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .slug(brand.getSlug())
                .description(brand.getDescription())
                .logoUrl(brand.getLogoUrl())
                .active(brand.isActive())
                .build();
    }

    public static ProductImageDto toImageDto(ProductImage image) {
        return ProductImageDto.builder()
                .id(image.getId())
                .url(image.getUrl())
                .altText(image.getAltText())
                .displayOrder(image.getDisplayOrder())
                .primary(image.isPrimary())
                .build();
    }

    public static ProductOfferDto toOfferDto(ProductOffer offer) {
        return ProductOfferDto.builder()
                .id(offer.getId())
                .sellerId(offer.getSeller().getId())
                .sellerName(offer.getSeller().getCompanyName() != null
                        ? offer.getSeller().getCompanyName()
                        : offer.getSeller().getUser().getEmail())
                .price(offer.getPrice())
                .stock(offer.getStock())
                .conditionLabel(offer.getConditionLabel())
                .build();
    }

    public static ProductSummaryDto toSummaryDto(Product product) {
        List<ProductOffer> activeOffers = getActiveOffers(product);
        BigDecimal minPrice = activeOffers.stream()
                .map(ProductOffer::getPrice)
                .min(Comparator.naturalOrder())
                .orElse(null);

        String imageUrl = product.getImages().stream()
                .filter(ProductImage::isPrimary)
                .findFirst()
                .or(() -> product.getImages().stream().findFirst())
                .map(ProductImage::getUrl)
                .orElse(null);

        return ProductSummaryDto.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(truncate(product.getDescription(), 200))
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .categorySlug(product.getCategory() != null ? product.getCategory().getSlug() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .brandSlug(product.getBrand() != null ? product.getBrand().getSlug() : null)
                .imageUrl(imageUrl)
                .minPrice(minPrice)
                .offerCount(activeOffers.size())
                .build();
    }

    public static ProductDetailDto toDetailDto(Product product) {
        List<ProductOffer> activeOffers = getActiveOffers(product);
        BigDecimal minPrice = activeOffers.stream()
                .map(ProductOffer::getPrice)
                .min(Comparator.naturalOrder())
                .orElse(null);

        return ProductDetailDto.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .category(toCategoryDto(product.getCategory(), false))
                .brand(toBrandDto(product.getBrand()))
                .images(product.getImages().stream()
                        .sorted(Comparator.comparingInt(ProductImage::getDisplayOrder))
                        .map(ProductMapper::toImageDto)
                        .toList())
                .offers(activeOffers.stream()
                        .sorted(Comparator.comparing(ProductOffer::getPrice))
                        .map(ProductMapper::toOfferDto)
                        .toList())
                .minPrice(minPrice)
                .build();
    }

    private static List<ProductOffer> getActiveOffers(Product product) {
        if (product.getOffers() == null) {
            return List.of();
        }
        return product.getOffers().stream()
                .filter(ProductOffer::isActive)
                .filter(o -> o.getStock() != null && o.getStock() > 0)
                .toList();
    }

    private static String truncate(String text, int max) {
        if (text == null) {
            return null;
        }
        return text.length() <= max ? text : text.substring(0, max) + "...";
    }
}
