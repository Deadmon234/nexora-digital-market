package com.nexora.shop.controller;

import com.nexora.shop.dto.ShopDto;
import com.nexora.shop.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
@Tag(name = "Boutiques")
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/{slug}")
    @Operation(summary = "Détail public d'une boutique")
    public ShopDto getShop(@PathVariable String slug) {
        return shopService.getPublicShop(slug);
    }
}
