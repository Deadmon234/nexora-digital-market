package com.nexora.inventory.controller;

import com.nexora.common.dto.PageResponse;
import com.nexora.inventory.dto.InventoryItemDto;
import com.nexora.inventory.dto.InventoryMovementDto;
import com.nexora.inventory.dto.StockAdjustRequest;
import com.nexora.inventory.service.InventoryService;
import com.nexora.seller.security.SellerAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers/me/inventory")
@RequiredArgsConstructor
@SellerAccess
@PreAuthorize("hasAuthority('ROLE_SELLER')")
@Tag(name = "Inventaire vendeur")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @Operation(summary = "Consulter le stock")
    public List<InventoryItemDto> getInventory() {
        return inventoryService.getInventory();
    }

    @PostMapping("/add")
    @Operation(summary = "Ajouter du stock")
    public InventoryItemDto addStock(@Valid @RequestBody StockAdjustRequest request) {
        return inventoryService.addStock(request.getOfferId(), request.getQuantity(), request.getReason());
    }

    @PostMapping("/decrease")
    @Operation(summary = "Diminuer le stock")
    public InventoryItemDto decreaseStock(@Valid @RequestBody StockAdjustRequest request) {
        return inventoryService.decreaseStock(request.getOfferId(), request.getQuantity(), request.getReason());
    }

    @GetMapping("/history")
    @Operation(summary = "Historique des mouvements")
    public PageResponse<InventoryMovementDto> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return inventoryService.getHistory(page, size);
    }
}
