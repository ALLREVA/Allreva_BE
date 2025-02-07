package com.backend.allreva.chatting.chat.group.infra;

import com.backend.allreva.chatting.chat.group.query.response.GroupChatDetailResponse;
import com.backend.allreva.chatting.chat.group.query.response.GroupChatOverviewResponse;
import com.backend.allreva.chatting.chat.group.query.response.GroupChatSummaryResponse;

import java.util.Optional;
import java.util.UUID;

public interface MemberGroupChatDslRepository {

    Optional<GroupChatOverviewResponse> findGroupChatOverview(
            UUID uuid
    );

    Optional<GroupChatDetailResponse> findGroupChatDetail(
            Long memberId,
            Long groupChatId
    );
}
