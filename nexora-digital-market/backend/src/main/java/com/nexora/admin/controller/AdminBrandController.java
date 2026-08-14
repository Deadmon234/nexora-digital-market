package com.nexora.admin.controller;

import com.nexora.admin.dto.BrandAdminRequest;
import com.nexora.admin.security.AdminAccess;
import com.nexora.admin.service.AdminBrandService;
import com.nexora.product.dto.BrandDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/brands")
@RequiredArgsConstructor
@AdminAccess
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(name = "Administration — Marques")
public class AdminBrandController {

    private final AdminBrandService adminBrandService;

    @GetMapping
    public List<BrandDto> findAll() {
        return adminBrandService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BrandDto create(@Valid @RequestBody BrandAdminRequest request) {
        return adminBrandService.create(request);
    }

    @PutMapping("/{id}")
    public BrandDto update(@PathVariable Long id, @Valid @RequestBody BrandAdminRequest request) {
        return adminBrandService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        adminBrandService.deactivate(id);
    }
}
