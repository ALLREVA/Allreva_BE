package com.backend.allreva.chatting.notification;

import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.chatting.chat.integration.model.value.PreviewMessage;
import lombok.Getter;

@Getter
public class PreviewMessageResponse {

    private final Long chatId;
    private final ChatType chatType;
    private final PreviewMessage previewMessage;

    public PreviewMessageResponse(
            final Long chatId,
            final ChatType chatType,
            final PreviewMessage previewMessage
    ) {
        this.chatId = chatId;
        this.chatType = chatType;
        this.previewMessage = previewMessage;
    }
}
