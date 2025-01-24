package com.backend.allreva.chatting.chat.single.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.chat.single.command.application.LeaveSingleChatRequest;
import com.backend.allreva.chatting.chat.single.command.application.MemberChatCommandService;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/chat/single")
@RestController
public class MemberChatController {

    private final MemberChatCommandService memberChatCommandService;

    @PostMapping
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
