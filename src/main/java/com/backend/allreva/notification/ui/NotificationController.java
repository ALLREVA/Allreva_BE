package com.backend.allreva.notification.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.notification.command.NotificationService;
import com.backend.allreva.notification.command.domain.Notification;
import com.backend.allreva.notification.command.dto.DeviceTokenRequest;
import com.backend.allreva.notification.command.dto.NotificationIdRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController implements NotificationSwagger {

    private final NotificationService notificationService;

    @GetMapping
    public Response<List<Notification>> getNotifications(
            @AuthMember final Member member,
            @RequestParam(required = false) final Long lastId,
            @RequestParam(defaultValue = "10") final int pageSize
    ) {
        return Response.onSuccess(notificationService.getNotificationsByRecipientId(member, lastId, pageSize));
    }

    @PatchMapping("/read")
    public Response<Void> markAsRead(
            @AuthMember final Member member,
            @RequestBody final NotificationIdRequest notificationIdRequest
    ) {
        notificationService.markAsRead(member, notificationIdRequest);
        return Response.onSuccess();
    }

    @PostMapping("/device-token")
    public Response<Void> registerDeviceToken(
            @AuthMember final Member member,
            @RequestBody final DeviceTokenRequest deviceTokenRequest
    ) {
        notificationService.registerDeviceToken(member, deviceTokenRequest);
        return Response.onSuccess();
    }

    @DeleteMapping("/device-token")
    public Response<Void> deleteDeviceToken(
            @RequestBody final Member member
    ) {
        notificationService.deleteDeviceToken(member);
        return Response.onSuccess();
    }
}
