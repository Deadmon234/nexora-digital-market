package com.nexora.order.service;

import com.nexora.common.enums.NotificationType;
import com.nexora.notification.service.NotificationService;
import com.nexora.order.entity.CustomerOrder;
import com.nexora.order.entity.SellerOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderNotificationService {

    private final NotificationService notificationService;

    public void notifyOrderCreated(CustomerOrder order) {
        notificationService.notifyFromTemplate(
                "ORDER_CREATED",
                NotificationType.ORDER_CREATED,
                order.getUser(),
                Map.of(
                        "orderNumber", order.getOrderNumber(),
                        "amount", order.getTotalAmount().toPlainString()
                ),
                "/account/orders/" + order.getId()
        );
    }

    public void notifyPaymentCompleted(CustomerOrder order) {
        notificationService.notifyFromTemplate(
                "PAYMENT_COMPLETED",
                NotificationType.PAYMENT_COMPLETED,
                order.getUser(),
                Map.of(
                        "orderNumber", order.getOrderNumber(),
                        "amount", order.getTotalAmount().toPlainString()
                ),
                "/account/orders/" + order.getId()
        );

        for (SellerOrder sellerOrder : order.getSellerOrders()) {
            notificationService.notifyFromTemplate(
                    "NEW_SELLER_ORDER",
                    NotificationType.NEW_SELLER_ORDER,
                    sellerOrder.getSeller().getUser(),
                    Map.of(
                            "orderNumber", order.getOrderNumber(),
                            "amount", sellerOrder.getSubtotal().toPlainString()
                    ),
                    "/seller/orders/" + sellerOrder.getId()
            );
        }
    }

    public void notifySellerOrderStatusUpdated(CustomerOrder order, String sellerName, String newStatus) {
        notificationService.notifyFromTemplate(
                "ORDER_STATUS_UPDATED",
                NotificationType.ORDER_STATUS_UPDATED,
                order.getUser(),
                Map.of(
                        "orderNumber", order.getOrderNumber(),
                        "sellerName", sellerName,
                        "status", newStatus
                ),
                "/account/orders/" + order.getId()
        );
    }
}
