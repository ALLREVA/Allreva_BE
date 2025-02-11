package com.backend.allreva.chatting.notification;

import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.chatting.chat.integration.model.value.PreviewMessage;
import com.backend.allreva.chatting.notification.event.ConnectedEvent;
import com.backend.allreva.common.event.Events;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@Service
public class MessageSseService {

    private final ConnectionRepository connectionRepository;

    public SseEmitter connect(final Long memberId) {
        Events.raise(new ConnectedEvent(memberId));
        return connectionRepository.connectByMemberId(memberId);
    }

    public void sendSummaryNotification(
            final Long chatId,
            final ChatType chatType,
            final PreviewMessage previewMessage
    ) {
        connectionRepository.sendNotification(chatId, chatType, previewMessage);
    }

}
