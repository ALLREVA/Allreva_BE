package com.backend.allreva.notification.command;

import com.backend.allreva.notification.command.domain.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Optional<Notification> findById(Long id);
    Optional<Notification> findByIdAndRecipientId(Long id, Long recipientId);
    List<Notification> findNotificationsByRecipientId(Long recipientId);
    List<Notification> findAll();

    void saveAll(List<Notification> notifications);
}
