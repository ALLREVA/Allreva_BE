package com.backend.allreva.chatting.message.query;

import com.backend.allreva.chatting.chat.integration.model.value.Participant;
import com.backend.allreva.chatting.message.domain.GroupMessage;
import com.backend.allreva.chatting.message.domain.SingleMessage;
import com.backend.allreva.chatting.message.domain.value.Content;

import java.time.LocalDateTime;

public record MessageResponse(

        long messageNumber,

        Content content,

        Participant sender,
        LocalDateTime sentAt
) {

    public static MessageResponse from(
            final SingleMessage singleMessage
    ) {
        return new MessageResponse(
                singleMessage.getMessageNumber(),
                singleMessage.getContent(),
                singleMessage.getSender(),
                singleMessage.getSentAt()
        );
    }

    public static MessageResponse from(
            final GroupMessage groupMessage
    ) {
        return new MessageResponse(
                groupMessage.getMessageNumber(),
                groupMessage.getContent(),
                groupMessage.getSender(),
                groupMessage.getSentAt()
        );
    }

}
