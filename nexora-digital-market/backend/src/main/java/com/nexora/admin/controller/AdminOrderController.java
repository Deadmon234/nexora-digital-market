package com.nexora.admin.controller;

import com.nexora.admin.security.AdminAccess;
import com.nexora.order.dto.OrderDetailDto;
import com.nexora.order.dto.OrderSummaryDto;
import com.nexora.order.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@AdminAccess
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(name = "Administration — Commandes")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderSummaryDto> findAll() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderDetailDto findById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
}
