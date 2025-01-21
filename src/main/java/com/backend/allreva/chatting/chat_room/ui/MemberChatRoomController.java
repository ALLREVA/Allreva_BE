package com.backend.allreva.chatting.chat_room.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.chat_room.model.ChatRoom;
import com.backend.allreva.chatting.chat_room.model.MemberChatRoomDoc;
import com.backend.allreva.chatting.chat_room.model.MemberChatRoomRepository;
import com.backend.allreva.chatting.infra.MessageSseService;
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
public class MemberChatRoomController {

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
    public Response<SortedSet<ChatRoom>> findParticipatingRooms(
            @AuthMember final Member member
    ) {
        MemberChatRoomDoc document = memberChatRoomRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new);

        SortedSet<ChatRoom> responses = document.getChatRooms();
        return Response.onSuccess(responses);
    }
}
