package com.nexora.notification.service;

import com.nexora.common.enums.NotificationChannel;
import com.nexora.common.enums.NotificationType;
import com.nexora.common.exception.ResourceNotFoundException;
import com.nexora.notification.dto.NotificationDto;
import com.nexora.notification.dto.UnreadCountDto;
import com.nexora.notification.entity.Notification;
import com.nexora.notification.repository.NotificationRepository;
import com.nexora.notification.repository.NotificationTemplateRepository;
import com.nexora.user.entity.User;
import com.nexora.user.security.UserContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final UserContextService userContextService;

    @Transactional
    public NotificationDto notify(User user, NotificationType type, String title, String message, String linkUrl) {
        Notification notification = Notification.builder()
                .user(user)
                .type(type)
                .title(title)
                .message(message)
                .linkUrl(linkUrl)
                .read(false)
                .build();
        return toDto(notificationRepository.save(notification));
    }

    @Transactional
    public void notifyFromTemplate(
            String templateCode,
            NotificationType type,
            User user,
            Map<String, String> variables,
            String linkUrl
    ) {
        String title = resolveTemplate(templateCode, NotificationChannel.IN_APP, variables, true);
        String message = resolveTemplate(templateCode, NotificationChannel.IN_APP, variables, false);
        notify(user, type, title, message, linkUrl);

        templateRepository.findByCodeAndChannelAndActiveTrue(templateCode, NotificationChannel.EMAIL)
                .ifPresent(template -> sendEmailStub(
                        user,
                        applyVariables(template.getSubject(), variables),
                        applyVariables(template.getBodyTemplate(), variables)
                ));

        templateRepository.findByCodeAndChannelAndActiveTrue(templateCode, NotificationChannel.SMS)
                .ifPresent(template -> sendSmsStub(user, applyVariables(template.getBodyTemplate(), variables)));
    }

    @Transactional(readOnly = true)
    public List<NotificationDto> getMyNotifications() {
        User user = userContextService.getCurrentUser();
        return notificationRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UnreadCountDto getUnreadCount() {
        User user = userContextService.getCurrentUser();
        return UnreadCountDto.builder()
                .count(notificationRepository.countByUserAndReadFalse(user))
                .build();
    }

    @Transactional
    public NotificationDto markAsRead(Long id) {
        User user = userContextService.getCurrentUser();
        Notification notification = notificationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Notification introuvable"));
        notification.setRead(true);
        return toDto(notificationRepository.save(notification));
    }

    @Transactional
    public void markAllAsRead() {
        User user = userContextService.getCurrentUser();
        notificationRepository.markAllAsReadForUser(user);
    }

    @Transactional
    public void deleteNotification(Long id) {
        User user = userContextService.getCurrentUser();
        Notification notification = notificationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Notification introuvable"));
        notificationRepository.delete(notification);
    }

    private String resolveTemplate(
            String code,
            NotificationChannel channel,
            Map<String, String> variables,
            boolean subject
    ) {
        return templateRepository.findByCodeAndChannelAndActiveTrue(code, channel)
                .map(t -> applyVariables(subject ? t.getSubject() : t.getBodyTemplate(), variables))
                .orElseGet(() -> applyVariables("${" + code + "}", variables));
    }

    private String applyVariables(String template, Map<String, String> variables) {
        if (template == null || variables == null) {
            return template;
        }
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    private void sendEmailStub(User user, String subject, String body) {
        log.info("[EMAIL] À {} — Sujet: {} — {}", user.getEmail(), subject, body);
    }

    private void sendSmsStub(User user, String message) {
        log.info("[SMS] À {} — {}", user.getEmail(), message);
    }

    private NotificationDto toDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .linkUrl(notification.getLinkUrl())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
