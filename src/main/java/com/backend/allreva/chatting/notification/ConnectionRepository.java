package com.backend.allreva.chatting.notification;

import com.backend.allreva.chatting.chat.group.command.domain.MemberGroupChatRepository;
import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
import com.backend.allreva.chatting.chat.integration.model.value.PreviewMessage;
import com.backend.allreva.chatting.chat.single.command.domain.MemberSingleChatRepository;
import com.backend.allreva.chatting.notification.event.TimedOutEvent;
import com.backend.allreva.common.event.Events;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@RequiredArgsConstructor
@Component
public class ConnectionRepository {

    public static final String SSE_NAME = "SSE_PreviewMessage";
    public static final String INIT_MESSAGE = "채팅 SSE 연결";

    public static final Long TIME_LIMIT = 60000 * 60 * 24L;

    private final MemberSingleChatRepository memberSingleChatRepository;
    private final MemberGroupChatRepository memberGroupChatRepository;

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();


    public SseEmitter connectByMemberId(final Long memberId) {
        SseEmitter emitter = emitters.computeIfAbsent(memberId,
                id -> createSseEmitter(memberId));

        sendPreviewMessage(emitter, new PreviewMessage(
                -1,
                INIT_MESSAGE,
                LocalDateTime.now()
        ));
        return emitter;
    }

    private SseEmitter createSseEmitter(final Long memberId) {
        SseEmitter emitter = new SseEmitter(TIME_LIMIT);
        TimedOutEvent timedOutEvent = new TimedOutEvent(memberId);

        emitter.onCompletion(() -> Events.raise(timedOutEvent));
        emitter.onError(e -> {
            Events.raise(timedOutEvent);
            log.error(e.getMessage());
        });
        emitter.onTimeout(() -> Events.raise(timedOutEvent));

        return emitter;
    }


    public void sendNotification(
            final Long chatId,
            final ChatType chatType,
            final PreviewMessage payload
    ) {
        if (chatType.equals(ChatType.SINGLE)) {
            Set<Long> memberIds = memberSingleChatRepository
                    .findAllMemberIdBySingleChatId(chatId);
            sendForEachMembers(memberIds, payload);
        }

        if (chatType.equals(ChatType.GROUP)) {
            Set<Long> memberIds = memberGroupChatRepository
                    .findAllMemberIdByGroupChatId(chatId);
            sendForEachMembers(memberIds, payload);
        }
    }

    private void sendForEachMembers(
            final Set<Long> memberIds,
            final PreviewMessage payload
    ) {
        memberIds.forEach(memberId -> {
            if (emitters.containsKey(memberId)) {
                SseEmitter emitter = emitters.get(memberId);
                sendPreviewMessage(emitter, payload);
            }
        });
    }

    private void sendPreviewMessage(
            final SseEmitter emitter,
            final PreviewMessage payload
    ) {
        try {
            emitter.send(SseEmitter.event()
                    .name(SSE_NAME)
                    .data(payload));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
    }

    public void disconnect(final Long memberId) {
        emitters.remove(memberId);
    }


}
