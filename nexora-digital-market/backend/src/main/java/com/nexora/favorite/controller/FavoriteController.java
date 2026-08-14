package com.nexora.favorite.controller;

import com.nexora.favorite.dto.FavoriteDto;
import com.nexora.favorite.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Tag(name = "Favoris")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    @Operation(summary = "Mes favoris")
    public List<FavoriteDto> getMyFavorites() {
        return favoriteService.getMyFavorites();
    }

    @GetMapping("/check/{productId}")
    @Operation(summary = "Vérifier si un produit est en favori")
    public Map<String, Boolean> checkFavorite(@PathVariable Long productId) {
        return favoriteService.isFavorite(productId);
    }

    @PostMapping("/{productId}")
    @Operation(summary = "Ajouter aux favoris")
    public ResponseEntity<FavoriteDto> addFavorite(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteService.addFavorite(productId));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Retirer des favoris")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long productId) {
        favoriteService.removeFavorite(productId);
        return ResponseEntity.noContent().build();
    }
}
