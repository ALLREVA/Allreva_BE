package com.backend.allreva.chatting.chat.group.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.chat.group.command.application.MemberGroupChatCommandService;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/chat/group")
@RestController
public class MemberGroupChatController {

    private final MemberGroupChatCommandService memberGroupChatCommandService;

    @PostMapping("/join")
    public Response<Long> joinGroupChat(
            @RequestBody final JoinGroupChatRequest request,
            @AuthMember final Member member
    ) {
        Long memberGroupChatId = memberGroupChatCommandService
                .add(request.uuid(), member.getId());

        return Response.onSuccess(memberGroupChatId);
    }

    @DeleteMapping("/leave")
    public Response<Void> leaveGroupChat(
            @RequestBody final LeaveGroupChatRequest request,
            @AuthMember final Member member
    ) {
        memberGroupChatCommandService
                .leave(request.groupChatId(), member.getId());
        return Response.onSuccess();
    }

}
