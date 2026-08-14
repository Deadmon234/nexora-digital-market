package com.nexora.order.service;

import com.nexora.order.entity.CustomerOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderNotificationService {

    public void notifyOrderCreated(CustomerOrder order) {
        log.info("Commande créée : {} pour l'utilisateur {} — montant {}",
                order.getOrderNumber(), order.getUser().getEmail(), order.getTotalAmount());
    }

    public void notifySellerOrderStatusUpdated(CustomerOrder order, String sellerName, String newStatus) {
        log.info("Sous-commande vendeur {} mise à jour : {} → statut {}",
                sellerName, order.getOrderNumber(), newStatus);
    }
}
