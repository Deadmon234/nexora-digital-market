package com.nexora.admin.controller;

import com.nexora.admin.dto.AdminShopDto;
import com.nexora.admin.dto.UpdateActiveRequest;
import com.nexora.admin.dto.UpdateShopStatusRequest;
import com.nexora.admin.security.AdminAccess;
import com.nexora.admin.service.AdminShopService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/shops")
@RequiredArgsConstructor
@AdminAccess
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(name = "Administration — Boutiques")
public class AdminShopController {

    private final AdminShopService adminShopService;

    @GetMapping
    public List<AdminShopDto> findAll() {
        return adminShopService.findAll();
    }

    @PatchMapping("/{id}/status")
    public AdminShopDto updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateShopStatusRequest request) {
        return adminShopService.updateStatus(id, request);
    }

    @PatchMapping("/{id}/active")
    public AdminShopDto updateActive(@PathVariable Long id, @Valid @RequestBody UpdateActiveRequest request) {
        return adminShopService.updateActive(id, request);
    }
}
