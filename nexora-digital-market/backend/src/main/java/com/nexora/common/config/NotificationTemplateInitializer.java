package com.nexora.common.config;

import com.nexora.common.enums.NotificationChannel;
import com.nexora.notification.entity.NotificationTemplate;
import com.nexora.notification.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Slf4j
@Profile({"dev", "test"})
public class NotificationTemplateInitializer {

    private final NotificationTemplateRepository templateRepository;

    @Bean
    CommandLineRunner initNotificationTemplates() {
        return args -> seedTemplates();
    }

    private void seedTemplates() {
        if (templateRepository.count() > 0) {
            return;
        }

        log.info("Initialisation des modèles de notification...");

        save("ORDER_CREATED", "Commande ${orderNumber} créée",
                "Votre commande ${orderNumber} d'un montant de ${amount} € a été créée. Finalisez le paiement.",
                NotificationChannel.IN_APP);
        saveEmail("ORDER_CREATED", "Commande ${orderNumber} créée",
                "Bonjour, votre commande ${orderNumber} (${amount} €) est en attente de paiement sur Nexora.");

        save("PAYMENT_COMPLETED", "Paiement confirmé",
                "Le paiement de ${amount} € pour la commande ${orderNumber} a été confirmé.",
                NotificationChannel.IN_APP);
        saveEmail("PAYMENT_COMPLETED", "Paiement confirmé — ${orderNumber}",
                "Votre paiement de ${amount} € pour la commande ${orderNumber} est confirmé.");

        save("ORDER_STATUS_UPDATED", "Mise à jour commande ${orderNumber}",
                "Le vendeur ${sellerName} a mis à jour votre commande ${orderNumber} : ${status}.",
                NotificationChannel.IN_APP);

        save("NEW_SELLER_ORDER", "Nouvelle commande ${orderNumber}",
                "Vous avez reçu une nouvelle commande ${orderNumber} d'un montant de ${amount} €.",
                NotificationChannel.IN_APP);
        saveEmail("NEW_SELLER_ORDER", "Nouvelle commande ${orderNumber}",
                "Nouvelle commande ${orderNumber} pour ${amount} € sur votre boutique Nexora.");

        log.info("Modèles de notification créés.");
    }

    private void save(String code, String subject, String body, NotificationChannel channel) {
        templateRepository.save(NotificationTemplate.builder()
                .code(code)
                .subject(subject)
                .bodyTemplate(body)
                .channel(channel)
                .active(true)
                .build());
    }

    private void saveEmail(String code, String subject, String body) {
        save(code, subject, body, NotificationChannel.EMAIL);
    }
}
