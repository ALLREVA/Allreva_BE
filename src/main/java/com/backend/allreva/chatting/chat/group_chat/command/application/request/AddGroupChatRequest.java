package com.backend.allreva.chatting.chat.group_chat.command.application.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record AddGroupChatRequest(

        @Size(min = 1, max = 20)
        String title,

        @Size(max = 50)
        String description,

        @Max(50)
        @Min(2)
        int capacity
) {
}
