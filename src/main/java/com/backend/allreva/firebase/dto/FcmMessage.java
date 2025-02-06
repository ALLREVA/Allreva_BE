package com.backend.allreva.firebase.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record FcmMessage(
        @JsonProperty("validate_only") boolean validateOnly,
        Message message
) {
    @Builder
    public record Message(
            Notification notification,
            String token
    ) {

    }

    @Builder
    public record Notification(
            String title,
            String body
    ) {

    }

    public static FcmMessage from(
            final String token,
            final boolean validateOnly,
            final String title,
            final String message
    ) {
        return FcmMessage.builder()
                .validateOnly(validateOnly)
                .message(Message.builder()
                        .notification(Notification.builder()
                                .title(title)
                                .body(message)
                                .build())
                        .token(token)
                        .build())
                .build();
    }
}
