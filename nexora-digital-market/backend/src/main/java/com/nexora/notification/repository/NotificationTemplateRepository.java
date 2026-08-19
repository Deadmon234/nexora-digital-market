package com.nexora.notification.repository;

import com.nexora.common.enums.NotificationChannel;
import com.nexora.notification.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    Optional<NotificationTemplate> findByCodeAndChannelAndActiveTrue(String code, NotificationChannel channel);
}
