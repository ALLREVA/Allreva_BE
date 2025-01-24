package com.backend.allreva.chatting.chat.single.infra;

import com.backend.allreva.chatting.chat.single.query.SingleChatDetailResponse;

public interface SingleChatDslRepository {

    SingleChatDetailResponse findSingleChatInfo(
            Long memberId,
            String memberNickname,
            String memberProfileUrl,
            Long singleChatId
    );
}
