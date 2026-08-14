package com.nexora.order.controller;

import com.nexora.order.dto.SellerOrderDto;
import com.nexora.order.dto.UpdateOrderStatusRequest;
import com.nexora.order.service.OrderService;
import com.nexora.seller.security.SellerAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers/me/orders")
@RequiredArgsConstructor
@SellerAccess
@PreAuthorize("hasAuthority('ROLE_SELLER')")
@Tag(name = "Commandes vendeur")
public class SellerOrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Commandes reçues")
    public List<SellerOrderDto> getMyOrders() {
        return orderService.getSellerOrders();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'une commande vendeur")
    public SellerOrderDto getMyOrder(@PathVariable Long id) {
        return orderService.getSellerOrder(id);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Mettre à jour le statut d'une commande")
    public SellerOrderDto updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateOrderStatusRequest request) {
        return orderService.updateSellerOrderStatus(id, request);
    }
}
