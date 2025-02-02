package com.backend.allreva.chatting.message.infra;

import com.backend.allreva.chatting.chat.integration.model.value.PreviewMessage;
import com.backend.allreva.chatting.message.query.MessageResponse;

import java.util.List;

public interface SingleMessageCustomRepository {

    PreviewMessage findPreviewMessageBySingleChatId(
            Long singleChatId
    );

    List<MessageResponse> findMessageResponsesWithinRange(
            Long singleChatId,
            long fromNumber,
            long toNumber
    );
}
