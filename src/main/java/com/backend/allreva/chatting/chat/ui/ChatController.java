package com.backend.allreva.chatting.chat.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.chat.query.model.chat_room.ChatRoomSummary;
import com.backend.allreva.chatting.chat.query.model.document.MemberChatRoomDoc;
import com.backend.allreva.chatting.chat.query.model.document.MemberChatRoomRepository;
import com.backend.allreva.chatting.message.infra.MessageSseService;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.SortedSet;

@RequiredArgsConstructor
@RestController
public class ChatController {

    private final MemberChatRoomRepository memberChatRoomRepository;
    private final MessageSseService messageSseService;

    @GetMapping(value = "/personal/stream/{chatId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeChatForSummaries(
            @PathVariable("chatId") final Long chatId,
            @AuthMember final Member member
    ) {
        SseEmitter emitter = new SseEmitter();
        return messageSseService.subscribeSingleChat(chatId, emitter);
    }

    @GetMapping(value = "/group/stream/{groupChatId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeGroupForSummaries(
            @PathVariable("groupChatId") final Long groupChatId,
            @AuthMember final Member member
    ) {
        SseEmitter emitter = new SseEmitter();
        return messageSseService.subscribeSingleChat(groupChatId, emitter);
    }

    @GetMapping("/chat/list")
    public Response<SortedSet<ChatRoomSummary>> findParticipatingRooms(
            @AuthMember final Member member
    ) {
        MemberChatRoomDoc document = memberChatRoomRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new);

        SortedSet<ChatRoomSummary> responses = document.getChatRoomSummaries();
        return Response.onSuccess(responses);
    }
}
