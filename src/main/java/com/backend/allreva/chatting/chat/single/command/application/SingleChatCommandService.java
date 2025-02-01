package com.backend.allreva.chatting.chat.single.command.application;

import com.backend.allreva.chatting.chat.single.command.application.dto.StartSingleChattingResponse;
import com.backend.allreva.chatting.chat.single.command.domain.*;
import com.backend.allreva.chatting.chat.single.command.domain.event.LeavedSingleChatEvent;
import com.backend.allreva.chatting.chat.single.command.domain.event.StartedSingleChatEvent;
import com.backend.allreva.common.event.Events;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SingleChatCommandService {

    private final SingleChatRepository singleChatRepository;
    private final MemberSingleChatRepository memberSingleChatRepository;

    @Transactional
    public StartSingleChattingResponse startSingleChatting(
            final Long memberId,
            final Long otherMemberId
    ) {

        // 이미 참여중인지 확인을 해줘야해..
//        memberSingleChatRepository

        SingleChat generatedSingleChat = singleChatRepository
                .save(new SingleChat());
        Long singleChatId = generatedSingleChat.getId();

        StartedSingleChatEvent startedEvent = new StartedSingleChatEvent(
                singleChatId,
                memberId,
                otherMemberId
        );
        Events.raise(startedEvent);

        return new StartSingleChattingResponse(singleChatId);
    }

    public void leaveSingleChatting(
            final Long memberId,
            final Long singleChatId
    ) {
        LeavedSingleChatEvent leavedEvent = new LeavedSingleChatEvent(
                memberId,
                singleChatId
        );
        Events.raise(leavedEvent);
    }

}
