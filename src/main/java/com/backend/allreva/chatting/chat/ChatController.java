package com.backend.allreva.chatting.chat;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.chat.command.ChatCommandService;
import com.backend.allreva.chatting.chat.domain.ChatRepository;
import com.backend.allreva.chatting.chat.ui.*;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController("/chat/room")
public class ChatController {

    private final ChatCommandService chatCommandService;
    private final ChatRepository chatRepository;


    /*// 기존 채팅방에 입장
    @GetMapping("/{roomId}")
    public Response<List<ExampleResponse>> enterChatRoom(
            @PathVariable("roomId") final String roomId,
            @RequestParam("lastChatIdForPaging") final String lastChatIdForPaging,
            @AuthMember final Member member
    ) {

    }*/


    /*// 개인 채팅방 삭제?
    @DeleteMapping
    public Response<Void> deleteChatRoom(
            @RequestBody final DeleteChatRequest request,
            @AuthMember final Member member
    ) {

    }


    @GetMapping("/{roomId}")
    public Response<String> findRoomInfo(
            @PathVariable("roomId") final String roomId,
            @AuthMember final Member member
    ) {

    }*/
}
