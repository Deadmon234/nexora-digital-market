package com.nexora.product.repository;

import com.nexora.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlugAndActiveTrue(String slug);
    List<Category> findByParentIsNullAndActiveTrueOrderByNameAsc();
    List<Category> findByParentAndActiveTrueOrderByNameAsc(Category parent);
    boolean existsBySlug(String slug);
    List<Category> findAllByOrderByNameAsc();
}
