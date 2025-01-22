package com.backend.allreva.chatting.chat.infra;

import com.backend.allreva.chatting.chat.query.RoomInfoDetail;

public interface MemberGroupChatDslRepository {
    RoomInfoDetail findGroupChatInfo(
            Long memberId,
            Long groupChatId
    );
}
