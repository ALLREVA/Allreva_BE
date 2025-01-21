package com.backend.allreva.chatting.chat_room.model;

import com.backend.allreva.chatting.member_chatting.Participant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageSummary {

    private Participant sender;
    private PreviewMessage previewMessage;

    /*@Builder
    public MessageSummary(
            final Participant sender,
            final long previewMessageNumber,
            final String previewText,
            final LocalDateTime sentAt
    ) {
        this.sender = sender;
        this.previewMessageNumber = previewMessageNumber;
        this.previewText = previewText;
        this.sentAt = sentAt;
    }*/
}
