package com.backend.allreva.chatting.chat.integration.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.chat.integration.model.ChatParticipantDoc;
import com.backend.allreva.chatting.chat.integration.model.ChatParticipantRepository;
import com.backend.allreva.chatting.chat.integration.model.value.ChatSummary;
import com.backend.allreva.chatting.message.infra.MessageSseService;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.SortedSet;

@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@RestController
public class ChatController {

    private final ChatParticipantRepository chatParticipantRepository;
    private final MessageSseService messageSseService;

    @GetMapping(value = "/single/stream/{chatId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeSingleChatForSummaries(
            @PathVariable("chatId") final Long chatId,
            @AuthMember final Member member
    ) {
        SseEmitter emitter = new SseEmitter(60000 * 60 * 24L); // 1h 연결
        return messageSseService.subscribeSingleChat(chatId, emitter);
    }

    @GetMapping(value = "/group/stream/{groupChatId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeGroupForSummaries(
            @PathVariable("groupChatId") final Long groupChatId,
            @AuthMember final Member member
    ) {
        SseEmitter emitter = new SseEmitter();
        return messageSseService.subscribeGroupChat(groupChatId, emitter);
    }

    @GetMapping("/list")
    public Response<SortedSet<ChatSummary>> findParticipatingRooms(
            @AuthMember final Member member
    ) {
        ChatParticipantDoc document = chatParticipantRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new);

        SortedSet<ChatSummary> responses = document.getChatSummaries();
        return Response.onSuccess(responses);
    }
}
