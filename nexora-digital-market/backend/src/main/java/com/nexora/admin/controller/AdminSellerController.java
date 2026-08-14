package com.nexora.admin.controller;

import com.nexora.admin.dto.*;
import com.nexora.admin.security.AdminAccess;
import com.nexora.admin.service.AdminSellerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sellers")
@RequiredArgsConstructor
@AdminAccess
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(name = "Administration — Vendeurs")
public class AdminSellerController {

    private final AdminSellerService adminSellerService;

    @GetMapping
    public List<AdminSellerDto> findAll() {
        return adminSellerService.findAll();
    }

    @GetMapping("/{id}")
    public AdminSellerDto findById(@PathVariable Long id) {
        return adminSellerService.findById(id);
    }

    @PatchMapping("/{id}/status")
    public AdminSellerDto updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateSellerStatusRequest request) {
        return adminSellerService.updateStatus(id, request);
    }

    @PatchMapping("/{id}/commission-rate")
    public AdminSellerDto updateCommissionRate(@PathVariable Long id, @Valid @RequestBody UpdateCommissionRateRequest request) {
        return adminSellerService.updateCommissionRate(id, request);
    }
}
