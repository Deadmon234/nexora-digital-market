package com.nexora.admin.service;

import com.nexora.admin.dto.CategoryAdminRequest;
import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.common.exception.ValidationException;
import com.nexora.product.dto.CategoryDto;
import com.nexora.product.entity.Category;
import com.nexora.product.mapper.ProductMapper;
import com.nexora.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        return categoryRepository.findAllByOrderByNameAsc().stream()
                .map(c -> ProductMapper.toCategoryDto(c, false))
                .toList();
    }

    @Transactional
    public CategoryDto create(CategoryAdminRequest request) {
        String slug = resolveSlug(request.getSlug(), request.getName());
        if (categoryRepository.existsBySlug(slug)) {
            throw new ValidationException("Slug déjà utilisé : " + slug);
        }
        Category parent = resolveParent(request.getParentId());
        Category category = Category.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .parent(parent)
                .active(request.getActive() == null || request.getActive())
                .build();
        return ProductMapper.toCategoryDto(categoryRepository.save(category), false);
    }

    @Transactional
    public CategoryDto update(Long id, CategoryAdminRequest request) {
        Category category = getCategory(id);
        category.setName(request.getName());
        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            category.setSlug(request.getSlug());
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getParentId() != null) {
            category.setParent(resolveParent(request.getParentId()));
        }
        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }
        return ProductMapper.toCategoryDto(categoryRepository.save(category), false);
    }

    @Transactional
    public void deactivate(Long id) {
        Category category = getCategory(id);
        category.setActive(false);
        categoryRepository.save(category);
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie introuvable"));
    }

    private Category resolveParent(Long parentId) {
        if (parentId == null) {
            return null;
        }
        return categoryRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie parente introuvable"));
    }

    static String resolveSlug(String slug, String name) {
        if (slug != null && !slug.isBlank()) {
            return slug;
        }
        return name.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}
