package com.nexora.product.service;

import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.product.dto.BrandDto;
import com.nexora.product.mapper.ProductMapper;
import com.nexora.product.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    @Transactional(readOnly = true)
    public List<BrandDto> findAll() {
        return brandRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(ProductMapper::toBrandDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public BrandDto findBySlug(String slug) {
        return brandRepository.findBySlugAndActiveTrue(slug)
                .map(ProductMapper::toBrandDto)
                .orElseThrow(() -> new ResourceNotFoundException("Marque introuvable : " + slug));
    }
}
