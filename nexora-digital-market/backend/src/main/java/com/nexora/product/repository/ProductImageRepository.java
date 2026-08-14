package com.nexora.product.repository;

import com.nexora.product.entity.Product;
import com.nexora.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductOrderByDisplayOrderAsc(Product product);
}
