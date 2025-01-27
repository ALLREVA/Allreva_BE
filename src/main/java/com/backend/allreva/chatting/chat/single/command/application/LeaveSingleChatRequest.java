package com.backend.allreva.chatting.chat.single.command.application;

import jakarta.validation.constraints.NotBlank;

public record LeaveSingleChatRequest(

        @NotBlank
        Long singleChatId
) {
}
