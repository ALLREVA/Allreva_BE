package com.backend.allreva.chatting.chat.single.command.application.request;

import jakarta.validation.constraints.NotBlank;

public record LeaveSingleChatRequest(

        @NotBlank
        Long singleChatId
) {
}
