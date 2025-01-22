package com.backend.allreva.chatting.member_chatting.member_chat.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.chat.query.SingleChatQueryService;
import com.backend.allreva.chatting.member_chatting.member_chat.command.LeaveSingleChatRequest;
import com.backend.allreva.chatting.member_chatting.member_chat.command.MemberChatCommandService;
import com.backend.allreva.chatting.member_chatting.member_chat.MemberChatQueryService;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberChatController {

    private final MemberChatCommandService memberChatCommandService;
    private final MemberChatQueryService memberChatQueryService;

    private final SingleChatQueryService chatRoomQueryService;


    public Response<Long> startSingleChatting(
            @RequestBody final StartSingleChattingRequest request,
            @AuthMember final Member member
    ) {
        Long chatId = memberChatCommandService.startSingleChatting(
                member.getId(),
                request.otherMemberId()
        );
        memberChatCommandService.startSingleChatting(
                request.otherMemberId(),
                member.getId()
        );
        return Response.onSuccess(chatId);
    }


    @DeleteMapping
    public Response<Void> leaveChatRoom(
            @RequestBody final LeaveSingleChatRequest request,
            @AuthMember final Member member
    ) {
        memberChatCommandService.leaveSingleChat(
                member.getId(),
                request.singleChatId()
        );
        return Response.onSuccess();
    }
}
