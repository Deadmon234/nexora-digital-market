package com.nexora.product.controller;

import com.nexora.common.dto.PageResponse;
import com.nexora.product.dto.BrandDto;
import com.nexora.product.dto.CategoryDto;
import com.nexora.product.dto.ProductDetailDto;
import com.nexora.product.dto.ProductOfferDto;
import com.nexora.product.dto.ProductSummaryDto;
import com.nexora.product.service.BrandService;
import com.nexora.product.service.CategoryService;
import com.nexora.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Marketplace")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    @GetMapping("/products")
    @Operation(summary = "Liste et recherche de produits")
    public PageResponse<ProductSummaryDto> listProducts(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "newest") String sort
    ) {
        return productService.search(q, category, brand, minPrice, maxPrice, page, size, sort);
    }

    @GetMapping("/products/{slug}")
    @Operation(summary = "Détail d'un produit")
    public ProductDetailDto getProduct(@PathVariable String slug) {
        return productService.findBySlug(slug);
    }

    @GetMapping("/products/{slug}/offers")
    @Operation(summary = "Offres vendeurs d'un produit")
    public List<ProductOfferDto> getProductOffers(@PathVariable String slug) {
        return productService.findOffersByProductSlug(slug);
    }

    @GetMapping("/categories")
    @Operation(summary = "Liste des catégories racines")
    public List<CategoryDto> listCategories() {
        return categoryService.findAllRootCategories();
    }

    @GetMapping("/categories/{slug}")
    @Operation(summary = "Détail d'une catégorie")
    public CategoryDto getCategory(@PathVariable String slug) {
        return categoryService.findBySlug(slug);
    }

    @GetMapping("/brands")
    @Operation(summary = "Liste des marques")
    public List<BrandDto> listBrands() {
        return brandService.findAll();
    }

    @GetMapping("/brands/{slug}")
    @Operation(summary = "Détail d'une marque")
    public BrandDto getBrand(@PathVariable String slug) {
        return brandService.findBySlug(slug);
    }

    @GetMapping("/search")
    @Operation(summary = "Recherche de produits (alias)")
    public PageResponse<ProductSummaryDto> search(
            @RequestParam String q,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "newest") String sort
    ) {
        return productService.search(q, category, brand, minPrice, maxPrice, page, size, sort);
    }
}
