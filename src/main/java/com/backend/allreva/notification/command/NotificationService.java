package com.backend.allreva.notification.command;

import com.backend.allreva.notification.command.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationSender notificationSender;

    public void sendMessage(NotificationRequest notificationRequest) {
        notificationSender.sendMessage(notificationRequest);
    }
}