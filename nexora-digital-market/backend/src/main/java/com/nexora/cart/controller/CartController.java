package com.nexora.cart.controller;

import com.nexora.cart.dto.AddCartItemRequest;
import com.nexora.cart.dto.CartDto;
import com.nexora.cart.dto.UpdateCartItemRequest;
import com.nexora.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Panier")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Mon panier")
    public CartDto getMyCart() {
        return cartService.getMyCart();
    }

    @PostMapping("/items")
    @Operation(summary = "Ajouter un article au panier")
    public ResponseEntity<CartDto> addItem(@Valid @RequestBody AddCartItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(request));
    }

    @PutMapping("/items/{id}")
    @Operation(summary = "Modifier la quantité d'un article")
    public CartDto updateItem(@PathVariable Long id, @Valid @RequestBody UpdateCartItemRequest request) {
        return cartService.updateItem(id, request);
    }

    @DeleteMapping("/items/{id}")
    @Operation(summary = "Retirer un article du panier")
    public CartDto removeItem(@PathVariable Long id) {
        return cartService.removeItem(id);
    }

    @DeleteMapping
    @Operation(summary = "Vider le panier")
    public CartDto clearCart() {
        return cartService.clearCart();
    }
}
