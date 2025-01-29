package com.backend.allreva.chatting.chat.single.command.application;

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

    @Transactional
    public Long startSingleChatting(
            final Long memberId,
            final Long otherMemberId
    ) {
        SingleChat generatedSingleChat = singleChatRepository
                .save(new SingleChat());
        Long singleChatId = generatedSingleChat.getId();

        StartedSingleChatEvent startedEvent = new StartedSingleChatEvent(
                singleChatId,
                memberId,
                otherMemberId
        );
        Events.raise(startedEvent);
        return singleChatId;
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
