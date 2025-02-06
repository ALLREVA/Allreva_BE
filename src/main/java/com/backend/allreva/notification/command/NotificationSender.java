package com.backend.allreva.notification.command;

public interface NotificationSender {

    void sendMessage(String deviceToken, String title, String message);
}
