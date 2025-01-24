package com.backend.allreva.chatting.chat.group.infra;

import com.backend.allreva.chatting.chat.group.query.GroupChatDetailResponse;

public interface MemberGroupChatDslRepository {
    GroupChatDetailResponse findGroupChatInfo(
            Long memberId,
            Long groupChatId
    );
}
