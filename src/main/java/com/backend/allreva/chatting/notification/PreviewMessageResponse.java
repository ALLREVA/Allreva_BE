package com.backend.allreva.chatting.notification;

import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.chatting.chat.integration.model.value.Participant;
import com.backend.allreva.chatting.chat.integration.model.value.PreviewMessage;
import lombok.Getter;

@Getter
public class PreviewMessageResponse {

    private final Long chatId;
    private final ChatType chatType;
    private final PreviewMessage previewMessage;
    private final Participant participant;

    public PreviewMessageResponse(
            final Long chatId,
            final ChatType chatType,
            final PreviewMessage previewMessage,
            final Participant participant
    ) {
        this.chatId = chatId;
        this.chatType = chatType;
        this.previewMessage = previewMessage;
        this.participant = participant;
    }
}
