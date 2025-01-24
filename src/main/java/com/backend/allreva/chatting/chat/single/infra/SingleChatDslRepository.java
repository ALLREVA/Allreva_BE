package com.backend.allreva.chatting.chat.single.infra;

import com.backend.allreva.chatting.chat.single.command.domain.OtherMember;
import com.backend.allreva.chatting.chat.single.query.SingleChatDetailResponse;

public interface SingleChatDslRepository {

    OtherMember findOtherMemberInfo(
            Long memberId,
            Long singleChatId
    );

    SingleChatDetailResponse findSingleChatInfo(
            Long memberId,
            String memberNickname,
            String memberProfileUrl,
            Long singleChatId
    );
}
