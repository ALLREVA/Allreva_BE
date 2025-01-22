package com.backend.allreva.chatting.chat.infra;

import com.backend.allreva.chatting.chat.query.RoomInfoDetail;

public interface SingleChatDslRepository {
    RoomInfoDetail findSingleChatInfo(
            Long memberId,
            Long singleChatId
    );
}
