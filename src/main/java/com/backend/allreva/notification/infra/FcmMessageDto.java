package com.backend.allreva.notification.infra;

import lombok.Builder;

@Builder
public record FcmMessageDto(
        boolean validateOnly,
        FcmMessageDto.Message message

) {
    @Builder
    public record Message(
            FcmMessageDto.Notification notification,
            String token
    ) {

    }

    @Builder
    public record Notification(
            String title,
            String body
    ) {

    }
}
