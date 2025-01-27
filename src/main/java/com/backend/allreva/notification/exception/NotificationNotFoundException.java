package com.backend.allreva.notification.exception;

import com.backend.allreva.common.exception.CustomException;

public class NotificationNotFoundException extends CustomException {

    public NotificationNotFoundException() {
        super(NotificationErrorCode.NOTIFICATION_NOT_FOUND);
    }
}
