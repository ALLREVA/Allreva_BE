package com.backend.allreva.chatting.chat.single.query;

import com.backend.allreva.chatting.chat.single.command.domain.SingleChatRepository;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SingleChatQueryService {

    private final SingleChatRepository singleChatRepository;

    public SingleChatDetailResponse findSingleChatInfo(
            final Member member,
            final Long singleChatId
    ) {
        return singleChatRepository
                .findSingleChatInfo(
                        member.getId(),
                        member.getMemberInfo().getNickname(),
                        member.getMemberInfo().getProfileImageUrl(),
                        singleChatId
                );
    }

}
