package com.backend.allreva.chatting.chat.query;

import com.backend.allreva.chatting.chat.single_chat.domain.SingleChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SingleChatQueryService {

    private final SingleChatRepository singleChatRepository;

    public RoomInfoDetail findSingleChatInfo(
            final Long memberId,
            final Long singleChatId
    ) {
        return singleChatRepository
                .findSingleChatInfo(memberId, singleChatId);
    }

}
