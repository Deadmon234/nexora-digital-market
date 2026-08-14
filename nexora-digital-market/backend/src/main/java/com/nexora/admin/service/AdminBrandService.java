package com.nexora.admin.service;

import com.nexora.admin.dto.BrandAdminRequest;
import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.common.exception.ValidationException;
import com.nexora.product.dto.BrandDto;
import com.nexora.product.entity.Brand;
import com.nexora.product.mapper.ProductMapper;
import com.nexora.product.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminBrandService {

    private final BrandRepository brandRepository;

    @Transactional(readOnly = true)
    public List<BrandDto> findAll() {
        return brandRepository.findAllByOrderByNameAsc().stream()
                .map(ProductMapper::toBrandDto)
                .toList();
    }

    @Transactional
    public BrandDto create(BrandAdminRequest request) {
        String slug = AdminCategoryService.resolveSlug(request.getSlug(), request.getName());
        if (brandRepository.existsBySlug(slug)) {
            throw new ValidationException("Slug déjà utilisé : " + slug);
        }
        Brand brand = Brand.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .logoUrl(request.getLogoUrl())
                .active(request.getActive() == null || request.getActive())
                .build();
        return ProductMapper.toBrandDto(brandRepository.save(brand));
    }

    @Transactional
    public BrandDto update(Long id, BrandAdminRequest request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marque introuvable"));
        brand.setName(request.getName());
        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            brand.setSlug(request.getSlug());
        }
        if (request.getDescription() != null) {
            brand.setDescription(request.getDescription());
        }
        if (request.getLogoUrl() != null) {
            brand.setLogoUrl(request.getLogoUrl());
        }
        if (request.getActive() != null) {
            brand.setActive(request.getActive());
        }
        return ProductMapper.toBrandDto(brandRepository.save(brand));
    }

    @Transactional
    public void deactivate(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marque introuvable"));
        brand.setActive(false);
        brandRepository.save(brand);
    }
}
