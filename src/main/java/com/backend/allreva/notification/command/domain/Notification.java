package com.backend.allreva.notification.command.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Notification {
    private Long id;
    private String title;
    private String message;
}
