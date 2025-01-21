package com.backend.allreva.chatting.group_chat.infra;

import com.backend.allreva.chatting.group_chat.query.GroupChatInfoResponse;

public interface MemberGroupChatDslRepository {
    GroupChatInfoResponse findGroupChatInfo(
            Long memberId,
            Long groupChatId
    );
}
