package com.nexora.admin.controller;

import com.nexora.admin.dto.AdminCommissionDto;
import com.nexora.admin.security.AdminAccess;
import com.nexora.admin.service.AdminCommissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/commissions")
@RequiredArgsConstructor
@AdminAccess
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(name = "Administration — Commissions")
public class AdminCommissionController {

    private final AdminCommissionService adminCommissionService;

    @GetMapping
    public List<AdminCommissionDto> findAll() {
        return adminCommissionService.findAll();
    }
}
