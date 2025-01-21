package com.backend.allreva.chatting.member_chatting.member_chat.ui;

import com.backend.allreva.chatting.chat.query.ChatQueryService;
import com.backend.allreva.chatting.member_chatting.member_chat.command.MemberChatCommandService;
import com.backend.allreva.chatting.member_chatting.member_chat.MemberChatQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberChatController {

    private final MemberChatCommandService memberChatCommandService;
    private final MemberChatQueryService memberChatQueryService;

    private final ChatQueryService chatRoomQueryService;




    /*@DeleteMapping
    public Response<Void> leaveChatRoom(
            @RequestBody final LeaveChatRequest request,
            @AuthMember final Member member
    ) {

    }*/
}
