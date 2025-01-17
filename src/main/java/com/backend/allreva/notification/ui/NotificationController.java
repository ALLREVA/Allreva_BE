package com.backend.allreva.notification.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.notification.command.dto.DeviceTokenRequest;
import com.backend.allreva.notification.command.domain.Notification;
import com.backend.allreva.notification.command.dto.NotificationIdRequest;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController implements NotificationSwagger {

    @Override
    @GetMapping
    public Response<List<Notification>> getNotifications(
            @AuthMember final Member member,
            final Long lastId,
            final LocalDate lastEndDate,
            final int pageSize
    ) {
        return null;
    }

    @Override
    @PostMapping("/device-token")
    public Response<Void> registerDeviceToken(
            @AuthMember final Member member,
            final DeviceTokenRequest deviceToken
    ) {
        return null;
    }

    @Override
    @DeleteMapping("/device-token")
    public Response<Void> deleteDeviceToken(
            @AuthMember final Member member
    ) {
        return null;
    }

    @Override
    @PatchMapping("/read")
    public Response<Void> markAsRead(
            @AuthMember final Member member,
            final NotificationIdRequest notificationIdRequest
    ) {
        return null;
    }
}
