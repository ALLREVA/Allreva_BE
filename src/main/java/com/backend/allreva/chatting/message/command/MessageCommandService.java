package com.backend.allreva.chatting.message.command;

import com.backend.allreva.chatting.counter.MessageCounterService;
import com.backend.allreva.chatting.message.domain.GroupMessage;
import com.backend.allreva.chatting.message.domain.GroupMessageRepository;
import com.backend.allreva.chatting.message.domain.SingleMessage;
import com.backend.allreva.chatting.message.domain.SingleMessageRepository;
import com.backend.allreva.chatting.message.domain.value.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MessageCommandService {

    private final SingleMessageRepository singleMessageRepository;
    private final GroupMessageRepository groupMessageRepository;

    private final MessageCounterService messageCounterService;

    @Transactional
    public SingleMessage saveSingleMessage(
            final Long chatId,
            final Content content
    ) {
        long number = messageCounterService.getSingleMessageNumber(chatId);
        SingleMessage singleMessage = new SingleMessage(chatId, number, content);
        return singleMessageRepository.save(singleMessage);
    }

    @Transactional
    public GroupMessage saveGroupMessage(
            final Long groupChatId,
            final Content content
    ) {
        long number = messageCounterService.getGroupMessageNumber(groupChatId);
        GroupMessage groupMessage = new GroupMessage(groupChatId, number, content);
        return groupMessageRepository.save(groupMessage);
    }
}
