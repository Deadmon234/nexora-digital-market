package com.nexora.order.controller;

import com.nexora.order.dto.CreateOrderRequest;
import com.nexora.order.dto.OrderDetailDto;
import com.nexora.order.dto.OrderSummaryDto;
import com.nexora.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Commandes client")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Créer une commande depuis le panier")
    public ResponseEntity<OrderDetailDto> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createFromCart(request));
    }

    @GetMapping
    @Operation(summary = "Mes commandes")
    public List<OrderSummaryDto> getMyOrders() {
        return orderService.getMyOrders();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'une commande")
    public OrderDetailDto getMyOrder(@PathVariable Long id) {
        return orderService.getMyOrder(id);
    }
}
