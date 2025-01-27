package com.backend.allreva.notification.infra;

import com.backend.allreva.notification.command.NotificationRepository;
import com.backend.allreva.notification.command.domain.Notification;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;
    private final NotificationJdbcRepository notificationJdbcRepository;

    @Override
    public Optional<Notification> findById(final Long id) {
        return notificationJpaRepository.findById(id);
    }

    @Override
    public Optional<Notification> findByIdAndRecipientId(final Long id, final Long recipientId) {
        return notificationJpaRepository.findByIdAndRecipientId(id, recipientId);
    }

    @Override
    public List<Notification> findNotificationsByRecipientId(final Long recipientId) {
        return notificationJpaRepository.findNotificationsByRecipientId(recipientId);
    }

    @Override
    public List<Notification> findAll() {
        return notificationJpaRepository.findAll();
    }

    @Override
    public void saveAll(final List<Notification> notifications) {
        notificationJdbcRepository.saveAllInBatch(notifications);
    }
}
