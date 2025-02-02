package com.backend.allreva.chatting.notification.event;

import com.backend.allreva.chatting.notification.ConnectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class ChatNotificationHandler {

    private final ConnectionRepository connectionRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onMessage(final TimedOutEvent event) {
        Long memberId = event.getMemberId();
        connectionRepository.disconnect(memberId);
    }
}
