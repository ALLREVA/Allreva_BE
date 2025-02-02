package com.backend.allreva.chatting.notification;

import com.backend.allreva.common.event.Events;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@EqualsAndHashCode(of = "memberId")
public class Emitter {

    public static final Long TIME_LIMIT = 60000 * 60 * 24L;

    private final Long memberId;
    @Getter
    private final SseEmitter sseEmitter;

    public Emitter(final Long memberId) {
        SseEmitter emitter = new SseEmitter(TIME_LIMIT);

        TimedOutEvent timedOutEvent = new TimedOutEvent(memberId);
        emitter.onCompletion(() -> Events.raise(timedOutEvent));
        emitter.onError(e -> {
            Events.raise(timedOutEvent);
            log.error(e.getMessage());
        });
        emitter.onTimeout(() -> Events.raise(timedOutEvent));

        this.memberId = memberId;
        this.sseEmitter = emitter;
    }

}
