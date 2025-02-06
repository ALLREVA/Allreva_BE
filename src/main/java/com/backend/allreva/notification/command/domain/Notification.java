package com.backend.allreva.notification.command.domain;

import com.backend.allreva.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String message;
    private Long recipientId;
    private boolean isRead;

    @Builder
    private Notification(String title, String message, Long recipientId) {
        this.title = title;
        this.message = message;
        this.recipientId = recipientId;
        this.isRead = false;
    }

    public static Notification from(String title, String message, Long recipientId) {
        return Notification.builder()
                .title(title)
                .message(message)
                .recipientId(recipientId)
                .build();
    }

    public void read() {
        this.isRead = true;
    }
}
