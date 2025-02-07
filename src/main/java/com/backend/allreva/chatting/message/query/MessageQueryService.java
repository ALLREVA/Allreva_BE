package com.backend.allreva.chatting.message.query;

import com.backend.allreva.chatting.chat.integration.model.ChatParticipantRepository;
import com.backend.allreva.chatting.chat.integration.model.EnteredChatEvent;
import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.chatting.message.domain.GroupMessageRepository;
import com.backend.allreva.chatting.message.domain.SingleMessageRepository;
import com.backend.allreva.common.event.Events;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageQueryService {

    public static final int PAGING_UNIT = 25;

    private final SingleMessageRepository singleMessageRepository;
    private final GroupMessageRepository groupMessageRepository;

    private final ChatParticipantRepository participantRepository;


    public EnterChatResponse findDefaultSingleMessages(
            final Long singleChatId,
            final Long memberId
    ) {
        Long lastReadMessageNumber = participantRepository
                .findLastReadMessageNumber(
                        memberId,
                        singleChatId,
                        ChatType.SINGLE
                );
        List<MessageResponse> messageResponses = singleMessageRepository.findMessageResponsesWithinRange(
                singleChatId,
                lastReadMessageNumber - PAGING_UNIT,
                lastReadMessageNumber + PAGING_UNIT
        );

        EnteredChatEvent enteredEvent = new EnteredChatEvent(
                singleChatId,
                ChatType.SINGLE,
                memberId,
                lastReadMessageNumber
        );
        Events.raise(enteredEvent);

        return new EnterChatResponse(
                memberId,
                lastReadMessageNumber,
                messageResponses
        );
    }

    public List<MessageResponse> findReadSingleMessages(
            final Long singleChatId,
            final long criteriaNumber
    ) {
        return singleMessageRepository.findMessageResponsesWithinRange(
                singleChatId,
                criteriaNumber - PAGING_UNIT,
                criteriaNumber
        );
    }

    public List<MessageResponse> findUnreadSingleMessages(
            final Long singleChatId,
            final long criteriaNumber
    ) {
        return singleMessageRepository.findMessageResponsesWithinRange(
                singleChatId,
                criteriaNumber,
                criteriaNumber + PAGING_UNIT
        );
    }


    public EnterChatResponse findDefaultGroupMessages(
            final Long groupChatId,
            final long memberId
    ) {
        Long lastReadMessageNumber = participantRepository
                .findLastReadMessageNumber(
                        memberId,
                        groupChatId,
                        ChatType.GROUP
                );
        List<MessageResponse> messageResponses = groupMessageRepository.findMessageResponsesWithinRange(
                groupChatId,
                lastReadMessageNumber - PAGING_UNIT,
                lastReadMessageNumber + PAGING_UNIT
        );
        return new EnterChatResponse(
                memberId,
                lastReadMessageNumber,
                messageResponses
        );
    }

    public List<MessageResponse> findReadGroupMessages(
            final Long groupChatId,
            final long criteriaNumber
    ) {
        return singleMessageRepository.findMessageResponsesWithinRange(
                groupChatId,
                criteriaNumber - PAGING_UNIT,
                criteriaNumber
        );
    }

    public List<MessageResponse> findUnreadGroupMessages(
            final Long groupChatId,
            final long criteriaNumber
    ) {
        return singleMessageRepository.findMessageResponsesWithinRange(
                groupChatId,
                criteriaNumber,
                criteriaNumber + PAGING_UNIT
        );
    }

}
