package com.backend.allreva.notification.infra;

import com.backend.allreva.notification.command.domain.Notification;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveAllInBatch(List<Notification> notifications) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO notification (title, message, recipient_id, is_read, created_at, updated_at, deleted_at) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                notifications,
                notifications.size(),
                (ps, notification) -> {
                    ps.setString(1, notification.getTitle());
                    ps.setString(2, notification.getMessage());
                    ps.setLong(3, notification.getRecipientId());
                    ps.setByte(4, (byte) 0);
                    LocalDateTime now = LocalDateTime.now();
                    ps.setObject(5, now);
                    ps.setObject(6, now);
                    ps.setNull(7, Types.TIMESTAMP);
                }
        );
    }
}
