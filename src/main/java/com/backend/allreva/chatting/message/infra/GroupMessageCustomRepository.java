package com.backend.allreva.chatting.message.infra;

import com.backend.allreva.chatting.message.query.MessageResponse;

import java.util.List;

public interface GroupMessageCustomRepository {
    List<MessageResponse> findMessageResponsesWithinRange(
            Long groupChatId,
            long fromNumber,
            long toNumber
    );
}
