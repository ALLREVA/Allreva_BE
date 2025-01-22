package com.backend.allreva.chatting.member_chatting.member_groupchat.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.member_chatting.member_groupchat.command.application.MemberGroupChatCommandService;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberGroupChatController {

    private final MemberGroupChatCommandService memberGroupChatCommandService;

    @PostMapping
    public Response<Long> joinGroupChat(
            @RequestBody final JoinGroupChatRequest request,
            @AuthMember final Member member
    ) {
        Long memberGroupChatId = memberGroupChatCommandService
                .add(request.groupChatId(), member.getId());

        return Response.onSuccess(memberGroupChatId);
    }

    @DeleteMapping
    public Response<Void> leaveGroupChat(
            @RequestBody final LeaveGroupChatRequest request,
            @AuthMember final Member member
    ) {
        memberGroupChatCommandService
                .leave(request.groupChatId(), member.getId());
        return Response.onSuccess();
    }

}
