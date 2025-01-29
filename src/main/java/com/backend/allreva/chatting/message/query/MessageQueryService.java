package com.backend.allreva.chatting.message.query;

import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.chatting.chat.integration.model.value.PreviewMessage;
import com.backend.allreva.chatting.message.domain.GroupMessageRepository;
import com.backend.allreva.chatting.message.domain.SingleMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageQueryService {

    public static final int PAGING_UNIT = 25;

    private final SingleMessageRepository singleMessageRepository;
    private final GroupMessageRepository groupMessageRepository;


    public List<MessageResponse> findDefaultSingleMessages(
            final Long singleChatId,
            final long lastReadMessageNumber
    ) {
        return singleMessageRepository.findMessageResponsesWithinRange(
                singleChatId,
                lastReadMessageNumber - PAGING_UNIT,
                lastReadMessageNumber + PAGING_UNIT
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


    public List<MessageResponse> findDefaultGroupMessages(
            final Long groupChatId,
            final long lastReadMessageNumber
    ) {
        return groupMessageRepository.findMessageResponsesWithinRange(
                groupChatId,
                lastReadMessageNumber - PAGING_UNIT,
                lastReadMessageNumber + PAGING_UNIT
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
