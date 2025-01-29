package com.backend.allreva.chatting.message.infra;

import com.backend.allreva.chatting.chat.integration.model.value.PreviewMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
public class MessageSseService {

    public static final String INIT_MESSAGE = "채팅에 연결되었습니다.";

    private final Map<Long, Set<SseEmitter>> chatConnections = new ConcurrentHashMap<>();
    private final Map<Long, Set<SseEmitter>> groupChatConnections = new ConcurrentHashMap<>();

    public SseEmitter subscribeSingleChat(
            final Long chatId,
            final SseEmitter emitter
    ) {
        chatConnections
                .computeIfAbsent(chatId, id -> ConcurrentHashMap.newKeySet())
                .add(emitter);


        emitter.onCompletion(() -> {
            unsubscribeSingleChat(chatId, emitter);
        });
        emitter.onError(e -> unsubscribeSingleChat(chatId, emitter));

        sendSse(emitter, "init", new PreviewMessage(
                -1,
                INIT_MESSAGE,
                LocalDateTime.now()
        ));
        return emitter;
    }

    private static void sendSse(
            final SseEmitter emitter,
            final String name,
            final PreviewMessage payload
    ) {
        try {
            emitter.send(SseEmitter.event()
                    .name(name)
                    .data(payload));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
    }

    public SseEmitter subscribeGroupChat(
            final Long groupChatId,
            final SseEmitter emitter
    ) {
        groupChatConnections
                .computeIfAbsent(groupChatId, id -> ConcurrentHashMap.newKeySet())
                .add(emitter);

        emitter.onCompletion(() -> unsubscribeGroupChat(groupChatId, emitter));
        emitter.onError(e -> unsubscribeGroupChat(groupChatId, emitter));

        sendSse(emitter, "init", new PreviewMessage(
                -1,
                INIT_MESSAGE,
                LocalDateTime.now()
        ));
        return emitter;
    }

    public void unsubscribeSingleChat(
            final Long chatId,
            final SseEmitter emitter
    )  {
        chatConnections
                .computeIfPresent(chatId, (id, emitters) -> {
                    emitters.remove(emitter);
                    return emitters;
                });
    }

    public void unsubscribeGroupChat(
            final Long groupChatId,
            final SseEmitter emitter
    )  {
        groupChatConnections
                .computeIfPresent(groupChatId, (id, emitters) -> {
                    emitters.remove(emitter);
                    return emitters;
                });
    }

    public void sendSummaryToSingleChat(
            final Long chatId,
            final PreviewMessage payload
    ) {
        Set<SseEmitter> emitters = chatConnections.get(chatId);
        sendSummary(emitters, payload);
    }

    public void sendSummaryToGroupChat(
            final Long groupChatId,
            final PreviewMessage payload
    ) {
        Set<SseEmitter> emitters = groupChatConnections.get(groupChatId);
        sendSummary(emitters, payload);
    }

    private void sendSummary(
            final Set<SseEmitter> emitters,
            final PreviewMessage payload
    ) {
        if (emitters.isEmpty()) {
            return;
        }
        emitters.forEach(emitter -> {
            sendSse(emitter, "sse_summary", payload);
        });
    }
}
