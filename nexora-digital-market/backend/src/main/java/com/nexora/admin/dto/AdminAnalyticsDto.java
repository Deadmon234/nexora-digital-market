package com.nexora.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class AdminAnalyticsDto {
    private long totalUsers;
    private long totalSellers;
    private long pendingSellers;
    private long totalProducts;
    private long totalOrders;
    private long pendingOrders;
    private BigDecimal totalRevenue;
    private BigDecimal totalCommissions;
    private long pendingWithdrawals;
}
