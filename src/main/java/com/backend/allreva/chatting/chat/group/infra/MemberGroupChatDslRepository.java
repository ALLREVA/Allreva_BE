package com.backend.allreva.chatting.chat.group.infra;

import com.backend.allreva.chatting.chat.group.query.GroupChatDetailResponse;
import com.backend.allreva.chatting.chat.group.query.GroupChatSummaryResponse;

import java.util.UUID;

public interface MemberGroupChatDslRepository {

    GroupChatDetailResponse findGroupChatDetail(
            Long memberId,
            Long groupChatId
    );

    GroupChatSummaryResponse findGroupChatSummary(UUID uuid);
}
