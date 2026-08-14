package com.nexora.seller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SellerDashboardDto {
    private long totalProducts;
    private long totalStock;
    private long lowStockProducts;
    private String shopName;
    private String sellerStatus;
}
