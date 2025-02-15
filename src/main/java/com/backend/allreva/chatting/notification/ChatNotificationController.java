package com.backend.allreva.chatting.notification;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.common.config.SecurityEndpointPaths;
import com.backend.allreva.member.command.domain.Member;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@RestController
public class ChatNotificationController {

    @Value("${url.front.protocol}")
    private String frontProtocol;
    @Value("${url.front.domain}")
    private String frontDomain;

    private final MessageSseService messageSseService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connectNotification(
            HttpServletResponse response,
            @AuthMember final Member member
    ) {
        response.setHeader("Access-Control-Allow-Origin", "https://localhost:3000");
        response.setHeader("Access-Control-Allow-Origin", frontProtocol + "://" + frontDomain);
        return messageSseService.connect(member.getId());
    }

}
