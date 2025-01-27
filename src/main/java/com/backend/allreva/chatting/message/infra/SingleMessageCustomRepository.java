package com.backend.allreva.chatting.message.infra;

import com.backend.allreva.chatting.message.query.MessageResponse;

import java.util.List;

public interface SingleMessageCustomRepository {

    List<MessageResponse> findMessageResponsesWithinRange(
            Long singleChatId,
            long fromNumber,
            long toNumber
    );
}
