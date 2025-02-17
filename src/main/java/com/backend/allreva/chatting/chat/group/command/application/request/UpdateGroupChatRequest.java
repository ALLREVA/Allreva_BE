package com.backend.allreva.chatting.chat.group.command.application.request;

import com.backend.allreva.common.model.Image;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateGroupChatRequest(

        @NotNull(message = "단체 채팅방을 선택해야합니다")
        Long groupChatId,

        @Size(min = 1, max = 20)
        String title,
        @Size(max = 50)
        String description,
        Image image

) {

}
