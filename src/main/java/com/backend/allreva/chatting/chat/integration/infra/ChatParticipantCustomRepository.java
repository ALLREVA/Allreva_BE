package com.backend.allreva.chatting.chat.integration.infra;

import com.backend.allreva.chatting.chat.integration.model.value.ChatType;

public interface ChatParticipantCustomRepository {
    Long findLastReadMessageNumber(
            Long memberId,
            Long chatId,
            ChatType chatType
    );
}
