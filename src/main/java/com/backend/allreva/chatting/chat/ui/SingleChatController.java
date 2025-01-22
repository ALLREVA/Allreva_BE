package com.backend.allreva.chatting.chat.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.chat.query.RoomInfoDetail;
import com.backend.allreva.chatting.chat.query.SingleChatQueryService;
import com.backend.allreva.chatting.chat.single_chat.command.SingleChatCommandService;
import com.backend.allreva.chatting.chat.single_chat.domain.SingleChatRepository;
import com.backend.allreva.chatting.chat.single_chat.ui.DeleteSingleChatRequest;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/single-chat")
@RestController
public class SingleChatController {

    private final SingleChatCommandService singleChatCommandService;
    private final SingleChatRepository singleChatRepository;
    private final SingleChatQueryService singleChatQueryService;

    @GetMapping("/{singleChatId}")
    public Response<RoomInfoDetail> findSingleChatInformation(
            @PathVariable("singleChatId") final Long singleChatId,
            @AuthMember final Member member
    ) {
        RoomInfoDetail response = singleChatQueryService
                .findSingleChatInfo(member.getId(), singleChatId);
        return Response.onSuccess(response);
    }


    /*// 기존 채팅방에 입장
    @GetMapping("/{roomId}")
    public Response<List<ExampleResponse>> enterChatRoom(
            @PathVariable("roomId") final String roomId,
            @RequestParam("lastChatIdForPaging") final String lastChatIdForPaging,
            @AuthMember final Member member
    ) {

    }*/

}
