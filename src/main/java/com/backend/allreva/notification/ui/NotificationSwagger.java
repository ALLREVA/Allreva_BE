package com.backend.allreva.notification.ui;

import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.notification.command.dto.DeviceTokenRequest;
import com.backend.allreva.notification.command.domain.Notification;
import com.backend.allreva.notification.command.dto.NotificationIdRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "알림 조회 API")
public interface NotificationSwagger {

    @Operation(
            summary = "사용자 알림 조회",
            description = """
                    알림 종류를 type에 입력해주세요. default값은 ALL입니다. lastId와 lastEndDate는 무한 스크롤을 위한 값입니다.
                    """
    )
    Response<List<Notification>> getNotifications(
            Member member,
            Long lastId,
            LocalDate lastEndDate,
            int pageSize
    );

    @Operation(
            summary = "사용자 디바이스 토큰 등록",
            description = "사용자의 디바이스 토큰을 등록합니다."
    )
    Response<Void> registerDeviceToken(
            Member member,
            DeviceTokenRequest deviceToken
    );

    @Operation(
            summary = "사용자 디바이스 토큰 삭제",
            description = "사용자의 디바이스 토큰을 삭제합니다."
    )
    Response<Void> deleteDeviceToken(
            Member member
    );

    @Operation(
            summary = "사용자 알림 읽음 표시",
            description = "알림을 클릭했을 경우 읽음 상태로 전환되는 API 입니다. 알림 ID로 알림을 찾아 읽음 표시로 변경됩니다."
    )
    Response<Void> markAsRead(
            Member member,
            NotificationIdRequest notificationIdRequest
    );
}
