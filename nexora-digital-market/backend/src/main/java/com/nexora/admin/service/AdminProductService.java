package com.nexora.admin.service;

import com.nexora.admin.dto.AdminProductDto;
import com.nexora.admin.dto.UpdateActiveRequest;
import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.product.entity.Product;
import com.nexora.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<AdminProductDto> findAll() {
        return productRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public AdminProductDto updateActive(Long id, UpdateActiveRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable"));
        product.setActive(request.getActive());
        return toDto(productRepository.save(product));
    }

    private AdminProductDto toDto(Product product) {
        return AdminProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .offerCount(product.getOffers() != null ? product.getOffers().size() : 0)
                .active(product.isActive())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
