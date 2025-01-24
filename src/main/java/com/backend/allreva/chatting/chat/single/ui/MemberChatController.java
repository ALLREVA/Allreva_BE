package com.backend.allreva.chatting.chat.single.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.chat.integration.application.ChatParticipantService;
import com.backend.allreva.chatting.chat.integration.model.value.ChatType;
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
    private final ChatParticipantService chatParticipantService;

    @PostMapping
    public Response<Long> startSingleChatting(
            @RequestBody final StartSingleChattingRequest request,
            @AuthMember final Member member
    ) {
        Long chatId = memberChatCommandService.startSingleChatting(
                member.getId(),
                request.otherMemberId()
        );
        memberChatCommandService.startOtherSingleChatting(
                chatId,
                member.getId(),
                request.otherMemberId()
        );

        chatParticipantService.addSingleChatSummary(
                member.getId(),
                chatId
        );
        chatParticipantService.addSingleChatSummary(
                request.otherMemberId(),
                chatId
        );
        return Response.onSuccess(chatId);
    }


    @DeleteMapping
    public Response<Void> leaveSingleChat(
            @RequestBody final LeaveSingleChatRequest request,
            @AuthMember final Member member
    ) {
        chatParticipantService.leaveChat(
                member.getId(),
                request.singleChatId(),
                ChatType.SINGLE
        );
        return Response.onSuccess();
    }
}
