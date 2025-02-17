package com.backend.allreva.notification.command;

import com.backend.allreva.common.event.NotificationEvent;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.notification.command.domain.Notification;
import com.backend.allreva.notification.command.dto.DeviceTokenRequest;
import com.backend.allreva.notification.command.dto.NotificationIdRequest;
import com.backend.allreva.notification.exception.NotificationNotFoundException;
import com.backend.allreva.notification.infra.DeviceTokenRepository;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationSender notificationSender;
    private final NotificationRepository notificationRepository;
    private final DeviceTokenRepository deviceTokenRepository;

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByRecipientId(final Member member, final Long lastId, final int pageSize) {
        return notificationRepository.findNotificationsByRecipientId(member.getId(), lastId, pageSize);
    }

    @Transactional
    public void markAsRead(final Member member, final NotificationIdRequest notificationIdRequest) {
        Notification notification = notificationRepository.findByIdAndRecipientId(notificationIdRequest.id(), member.getId())
                .orElseThrow(NotificationNotFoundException::new);
        notification.read();
    }

    public void registerDeviceToken(final Member member, final DeviceTokenRequest deviceTokenRequest) {
        deviceTokenRepository.save(member.getId(), deviceTokenRequest.deviceToken());
    }

    public void deleteDeviceToken(final Member member) {
        deviceTokenRepository.delete(member.getId());
    }

    @Async
    @TransactionalEventListener
    public void sendMessage(final NotificationEvent event) {
        // device token 가져오기 (지금은 fcm 고정)
        List<String> deviceTokens = Optional.ofNullable(deviceTokenRepository.findTokensByMemberIds(event.getRecipientIds()))
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Objects::nonNull)
                .toList();
        if (deviceTokens.isEmpty()) {
            log.debug("디바이스 토큰이 없습니다.");
            return;
        }
        // 알림 메세지 보내기
        // TODO: 알림 전송 실패 시 대책 마련
        deviceTokens.forEach(fcmToken ->
                notificationSender.sendMessage(fcmToken, event.getTitle(), event.getMessage())
        );
        log.debug("알림 메세지 전송 완료");
        // 알림 메세지 저장
        List<Notification> notificationEntities = event.getRecipientIds().stream()
                .map(recipientId -> Notification.from(event.getTitle(), event.getMessage(), recipientId))
                .toList();
        notificationRepository.saveAll(notificationEntities);
        log.debug("알림 메세지 저장 완료");
    }
}