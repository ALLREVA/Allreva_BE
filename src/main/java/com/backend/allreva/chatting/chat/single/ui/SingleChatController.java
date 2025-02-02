package com.backend.allreva.chatting.chat.single.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.chat.single.command.application.dto.LeaveSingleChatRequest;
import com.backend.allreva.chatting.chat.single.command.application.SingleChatCommandService;
import com.backend.allreva.chatting.chat.single.command.application.dto.StartSingleChattingRequest;
import com.backend.allreva.chatting.chat.single.query.SingleChatDetailResponse;
import com.backend.allreva.chatting.chat.single.query.SingleChatQueryService;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/chat/single")
@RestController
public class SingleChatController {

    private final SingleChatCommandService singleChatCommandService;
    private final SingleChatQueryService singleChatQueryService;

    @GetMapping("/{singleChatId}")
    public Response<SingleChatDetailResponse> findSingleChatInformation(
            @PathVariable("singleChatId") final Long singleChatId,
            @AuthMember final Member member
    ) {
        SingleChatDetailResponse response = singleChatQueryService
                .findSingleChatInfo(member, singleChatId);
        return Response.onSuccess(response);
    }

    @PostMapping
    public Response<Long> startSingleChatting(
            @RequestBody final StartSingleChattingRequest request,
            @AuthMember final Member member
    ) {
        Long memberId = member.getId();
        Long otherMemberId = request.otherMemberId();

        Long singleChatId = singleChatCommandService
                .startSingleChatting(memberId, otherMemberId);

        return Response.onSuccess(singleChatId);
    }


    @DeleteMapping
    public Response<Void> leaveSingleChat(
            @RequestBody final LeaveSingleChatRequest request,
            @AuthMember final Member member
    ) {
        singleChatCommandService.leaveSingleChatting(
                member.getId(),
                request.singleChatId()
        );
        return Response.onSuccess();
    }

}
