package com.backend.allreva.chatting.chat.single.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.chat.single.query.SingleChatDetailResponse;
import com.backend.allreva.chatting.chat.single.query.SingleChatQueryService;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/chat/single")
@RestController
public class SingleChatController {

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

}
