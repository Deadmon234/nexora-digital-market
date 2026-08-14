package com.nexora.product.service;

import com.nexora.common.dto.PageResponse;
import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.product.dto.ProductDetailDto;
import com.nexora.product.dto.ProductOfferDto;
import com.nexora.product.dto.ProductSummaryDto;
import com.nexora.product.entity.Product;
import com.nexora.product.mapper.ProductMapper;
import com.nexora.product.repository.ProductOfferRepository;
import com.nexora.product.repository.ProductRepository;
import com.nexora.product.specification.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOfferRepository productOfferRepository;

    @Transactional(readOnly = true)
    public PageResponse<ProductSummaryDto> search(
            String query,
            String categorySlug,
            String brandSlug,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sort
    ) {
        Sort sortOrder = resolveSort(sort);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Specification<Product> spec = ProductSpecifications.withFilters(query, categorySlug, brandSlug, minPrice, maxPrice);

        Page<ProductSummaryDto> result = productRepository.findAll(spec, pageable)
                .map(ProductMapper::toSummaryDto);

        return PageResponse.from(result);
    }

    @Transactional(readOnly = true)
    public ProductDetailDto findBySlug(String slug) {
        Product product = productRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable : " + slug));
        return ProductMapper.toDetailDto(product);
    }

    @Transactional(readOnly = true)
    public List<ProductOfferDto> findOffersByProductSlug(String slug) {
        Product product = productRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable : " + slug));
        return productOfferRepository.findByProductAndActiveTrueOrderByPriceAsc(product).stream()
                .map(ProductMapper::toOfferDto)
                .toList();
    }

    private Sort resolveSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        return switch (sort) {
            case "name_asc" -> Sort.by(Sort.Direction.ASC, "name");
            case "name_desc" -> Sort.by(Sort.Direction.DESC, "name");
            case "newest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
    }
}
