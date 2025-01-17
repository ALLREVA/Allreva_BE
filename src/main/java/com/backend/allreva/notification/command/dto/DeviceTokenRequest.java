package com.backend.allreva.notification.command.dto;

import lombok.Builder;

@Builder
public record DeviceTokenRequest(
        String deviceToken
) {

}
