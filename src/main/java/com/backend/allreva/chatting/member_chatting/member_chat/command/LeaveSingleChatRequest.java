package com.backend.allreva.chatting.member_chatting.member_chat.command;

import jakarta.validation.constraints.NotBlank;

public record LeaveSingleChatRequest(

        @NotBlank
        Long singleChatId
) {
}
