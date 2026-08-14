package com.nexora.product.service;

import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.product.dto.CategoryDto;
import com.nexora.product.entity.Category;
import com.nexora.product.mapper.ProductMapper;
import com.nexora.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> findAllRootCategories() {
        return categoryRepository.findByParentIsNullAndActiveTrueOrderByNameAsc().stream()
                .map(c -> ProductMapper.toCategoryDto(c, true))
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryDto findBySlug(String slug) {
        Category category = categoryRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable : " + slug));
        return ProductMapper.toCategoryDto(category, true);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findSubcategories(String parentSlug) {
        Category parent = categoryRepository.findBySlugAndActiveTrue(parentSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable : " + parentSlug));
        return categoryRepository.findByParentAndActiveTrueOrderByNameAsc(parent).stream()
                .map(c -> ProductMapper.toCategoryDto(c, false))
                .toList();
    }
}
