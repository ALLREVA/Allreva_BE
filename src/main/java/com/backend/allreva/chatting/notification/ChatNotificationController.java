package com.backend.allreva.chatting.notification;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@RestController
public class ChatNotificationController {

    private final MessageSseService messageSseService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connectNotification(
            @AuthMember final Member member
    ) {
        return messageSseService.connect(member.getId());
    }

}
