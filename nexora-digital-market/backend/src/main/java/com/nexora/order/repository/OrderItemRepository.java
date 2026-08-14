package com.nexora.order.repository;

import com.nexora.order.entity.OrderItem;
import com.nexora.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
            SELECT COUNT(oi) > 0 FROM OrderItem oi
            JOIN oi.sellerOrder so
            JOIN so.order o
            WHERE o.user = :user
            AND oi.productSlug = :productSlug
            AND o.status NOT IN (com.nexora.common.enums.OrderStatus.PENDING, com.nexora.common.enums.OrderStatus.CANCELLED)
            """)
    boolean existsPurchasedByUserAndProductSlug(@Param("user") User user, @Param("productSlug") String productSlug);

    @Query("""
            SELECT COUNT(oi) > 0 FROM OrderItem oi
            JOIN oi.sellerOrder so
            JOIN so.order o
            WHERE o.user = :user
            AND so.seller.id = :sellerId
            AND o.status NOT IN (com.nexora.common.enums.OrderStatus.PENDING, com.nexora.common.enums.OrderStatus.CANCELLED)
            """)
    boolean existsPurchasedFromSeller(@Param("user") User user, @Param("sellerId") Long sellerId);
}
