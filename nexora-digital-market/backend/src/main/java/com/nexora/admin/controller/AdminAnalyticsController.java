package com.nexora.admin.controller;

import com.nexora.admin.dto.AdminAnalyticsDto;
import com.nexora.admin.security.AdminAccess;
import com.nexora.admin.service.AdminAnalyticsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/analytics")
@RequiredArgsConstructor
@AdminAccess
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@Tag(name = "Administration — Statistiques")
public class AdminAnalyticsController {

    private final AdminAnalyticsService adminAnalyticsService;

    @GetMapping
    public AdminAnalyticsDto getStats() {
        return adminAnalyticsService.getDashboardStats();
    }
}
