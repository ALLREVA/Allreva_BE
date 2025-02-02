package com.backend.allreva.chatting.message.infra;

import com.backend.allreva.chatting.chat.integration.model.value.PreviewMessage;
import com.backend.allreva.chatting.message.query.MessageResponse;

import java.util.List;

public interface GroupMessageCustomRepository {
    PreviewMessage findPreviewMessageByGroupChatId(
            Long groupChatId
    );

    List<MessageResponse> findMessageResponsesWithinRange(
            Long groupChatId,
            long fromNumber,
            long toNumber
    );
}
