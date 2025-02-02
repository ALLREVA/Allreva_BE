package com.backend.allreva.chatting.notification;

import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.chatting.chat.integration.model.value.PreviewMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@Service
public class MessageSseService {

    private final ConnectionRepository connectionRepository;

    public SseEmitter connect(final Long memberId) {
        return connectionRepository.connectByMemberId(memberId);
    }

    public void sendSummaryNotification(
            final Long chatId,
            final ChatType chatType,
            final PreviewMessage payload
    ) {
        connectionRepository.sendNotification(chatId, chatType, payload);
    }

}
