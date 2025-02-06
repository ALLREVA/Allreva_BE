package com.backend.allreva.firebase.service;

import com.backend.allreva.firebase.dto.FcmMessage;
import com.backend.allreva.firebase.infra.FcmClient;
import com.backend.allreva.firebase.util.FcmTokenUtils;
import com.backend.allreva.notification.command.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmSender implements NotificationSender {

    private final FcmClient fcmClient;

    @Value("${fcm.project-id}")
    private String projectId;

    public void sendMessage(
            final String deviceToken,
            final String title,
            final String message
    ) {
        String accessToken = FcmTokenUtils.getAccessToken();
        String authorizationHeader = "Bearer " + accessToken;
        FcmMessage fcmMessage = FcmMessage.from(deviceToken, false, title, message);
        fcmClient.sendMessage(
                authorizationHeader,
                fcmMessage,
                projectId
        );
        log.info("FCM 메시지 전송 성공");
    }
}