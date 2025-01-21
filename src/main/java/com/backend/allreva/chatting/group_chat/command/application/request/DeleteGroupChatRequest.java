package com.backend.allreva.chatting.group_chat.command.application.request;

import jakarta.validation.constraints.NotBlank;

public record DeleteGroupChatRequest(

        @NotBlank
        Long groupChatId
) {
}
