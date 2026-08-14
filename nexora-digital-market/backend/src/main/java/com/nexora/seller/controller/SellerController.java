package com.nexora.seller.controller;

import com.nexora.seller.dto.*;
import com.nexora.seller.security.SellerAccess;
import com.nexora.seller.service.SellerProductService;
import com.nexora.seller.service.SellerService;
import com.nexora.shop.dto.ShopDto;
import com.nexora.shop.dto.ShopRequest;
import com.nexora.shop.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
@Tag(name = "Vendeur")
public class SellerController {

    private final SellerService sellerService;
    private final ShopService shopService;
    private final SellerProductService sellerProductService;

    @PostMapping("/apply")
    @Operation(summary = "Postuler comme vendeur")
    public ResponseEntity<SellerProfileDto> apply(@Valid @RequestBody SellerApplyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sellerService.applyAsSeller(request));
    }

    @GetMapping("/me")
    @SellerAccess
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @Operation(summary = "Mon profil vendeur")
    public SellerProfileDto getMyProfile() {
        return sellerService.getMyProfile();
    }

    @PutMapping("/me")
    @SellerAccess
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @Operation(summary = "Modifier mon profil vendeur")
    public SellerProfileDto updateMyProfile(@Valid @RequestBody UpdateSellerProfileRequest request) {
        return sellerService.updateMyProfile(request);
    }

    @GetMapping("/me/dashboard")
    @SellerAccess
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @Operation(summary = "Tableau de bord vendeur")
    public SellerDashboardDto getDashboard() {
        return sellerService.getDashboard();
    }

    @GetMapping("/me/shop")
    @SellerAccess
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @Operation(summary = "Ma boutique")
    public ShopDto getMyShop() {
        return shopService.getMyShop();
    }

    @PutMapping("/me/shop")
    @SellerAccess
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @Operation(summary = "Créer ou modifier ma boutique")
    public ShopDto updateMyShop(@Valid @RequestBody ShopRequest request) {
        return shopService.createOrUpdateShop(request);
    }

    @GetMapping("/me/products")
    @SellerAccess
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @Operation(summary = "Mes produits")
    public List<SellerProductDto> getMyProducts() {
        return sellerProductService.getMyProducts();
    }

    @GetMapping("/me/products/{id}")
    @SellerAccess
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @Operation(summary = "Détail d'un produit vendeur")
    public SellerProductDto getMyProduct(@PathVariable Long id) {
        return sellerProductService.getMyProduct(id);
    }

    @PostMapping("/me/products")
    @SellerAccess
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @Operation(summary = "Ajouter un produit")
    public ResponseEntity<SellerProductDto> createProduct(@Valid @RequestBody SellerProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sellerProductService.createProduct(request));
    }

    @PutMapping("/me/products/{id}")
    @SellerAccess
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @Operation(summary = "Modifier un produit")
    public SellerProductDto updateProduct(@PathVariable Long id, @Valid @RequestBody SellerProductRequest request) {
        return sellerProductService.updateProduct(id, request);
    }

    @DeleteMapping("/me/products/{id}")
    @SellerAccess
    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @Operation(summary = "Supprimer un produit (désactivation)")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        sellerProductService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
