package com.nexora.admin.controller;

import com.nexora.admin.dto.AdminProductDto;
import com.nexora.admin.dto.UpdateActiveRequest;
import com.nexora.admin.security.AdminAccess;
import com.nexora.admin.service.AdminProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@AdminAccess
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(name = "Administration — Produits")
public class AdminProductController {

    private final AdminProductService adminProductService;

    @GetMapping
    public List<AdminProductDto> findAll() {
        return adminProductService.findAll();
    }

    @PatchMapping("/{id}/active")
    public AdminProductDto updateActive(@PathVariable Long id, @Valid @RequestBody UpdateActiveRequest request) {
        return adminProductService.updateActive(id, request);
    }
}
