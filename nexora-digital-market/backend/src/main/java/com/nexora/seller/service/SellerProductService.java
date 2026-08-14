package com.nexora.seller.service;

import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.common.exception.ValidationException;
import com.nexora.common.util.SlugUtils;
import com.nexora.product.entity.*;
import com.nexora.product.repository.*;
import com.nexora.seller.dto.SellerProductDto;
import com.nexora.seller.dto.SellerProductRequest;
import com.nexora.seller.entity.Seller;
import com.nexora.seller.security.SellerContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerProductService {

    private final SellerContextService sellerContextService;
    private final ProductRepository productRepository;
    private final ProductOfferRepository productOfferRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Transactional(readOnly = true)
    public List<SellerProductDto> getMyProducts() {
        Seller seller = sellerContextService.getCurrentSeller();
        return productOfferRepository.findBySellerOrderByCreatedAtDesc(seller).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public SellerProductDto getMyProduct(Long offerId) {
        var offer = sellerContextService.requireOwnedOffer(offerId);
        return toDto(offer);
    }

    @Transactional
    public SellerProductDto createProduct(SellerProductRequest request) {
        Seller seller = sellerContextService.requireApprovedSeller();

        String slug = generateUniqueProductSlug(request.getName());
        Product product = Product.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .category(resolveCategory(request.getCategoryId()))
                .brand(resolveBrand(request.getBrandId()))
                .active(true)
                .build();

        if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            product.getImages().add(ProductImage.builder()
                    .product(product)
                    .url(request.getImageUrl())
                    .altText(request.getName())
                    .displayOrder(0)
                    .primary(true)
                    .build());
        }

        product = productRepository.save(product);

        ProductOffer offer = ProductOffer.builder()
                .product(product)
                .seller(seller)
                .price(request.getPrice())
                .stock(request.getStock())
                .conditionLabel(request.getConditionLabel() != null ? request.getConditionLabel() : "Neuf")
                .active(true)
                .build();

        return toDto(productOfferRepository.save(offer));
    }

    @Transactional
    public SellerProductDto updateProduct(Long offerId, SellerProductRequest request) {
        ProductOffer offer = sellerContextService.requireOwnedOffer(offerId);
        Product product = offer.getProduct();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        if (request.getCategoryId() != null) {
            product.setCategory(resolveCategory(request.getCategoryId()));
        }
        if (request.getBrandId() != null) {
            product.setBrand(resolveBrand(request.getBrandId()));
        }

        offer.setPrice(request.getPrice());
        offer.setStock(request.getStock());
        if (request.getConditionLabel() != null) {
            offer.setConditionLabel(request.getConditionLabel());
        }

        productRepository.save(product);
        return toDto(productOfferRepository.save(offer));
    }

    @Transactional
    public void deleteProduct(Long offerId) {
        ProductOffer offer = sellerContextService.requireOwnedOffer(offerId);
        offer.setActive(false);
        productOfferRepository.save(offer);
    }

    private Category resolveCategory(Long id) {
        if (id == null) return null;
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Catégorie introuvable"));
    }

    private Brand resolveBrand(Long id) {
        if (id == null) return null;
        return brandRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Marque introuvable"));
    }

    private String generateUniqueProductSlug(String name) {
        String base = SlugUtils.toSlug(name);
        String slug = base;
        int counter = 1;
        while (productRepository.existsBySlug(slug)) {
            slug = base + "-" + counter++;
        }
        return slug;
    }

    private SellerProductDto toDto(ProductOffer offer) {
        Product product = offer.getProduct();
        String imageUrl = product.getImages().stream()
                .filter(ProductImage::isPrimary)
                .findFirst()
                .or(() -> product.getImages().stream().findFirst())
                .map(ProductImage::getUrl)
                .orElse(null);

        return SellerProductDto.builder()
                .offerId(offer.getId())
                .productId(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .imageUrl(imageUrl)
                .price(offer.getPrice())
                .stock(offer.getStock())
                .conditionLabel(offer.getConditionLabel())
                .active(offer.isActive())
                .build();
    }
}
