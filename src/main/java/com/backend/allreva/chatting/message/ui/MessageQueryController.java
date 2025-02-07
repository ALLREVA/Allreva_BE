package com.backend.allreva.chatting.message.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.message.query.EnterChatResponse;
import com.backend.allreva.chatting.message.query.MessageQueryService;
import com.backend.allreva.chatting.message.query.MessageResponse;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/message")
@RestController
public class MessageQueryController {

    private final MessageQueryService messageQueryService;

    @GetMapping("/single/enter")
    public Response<EnterChatResponse> enterSingleChat(
            @RequestParam("singleChatId") final Long singleChatId,
            @AuthMember final Member member
    ) {
        EnterChatResponse response = messageQueryService
                .findDefaultSingleMessages(singleChatId, member.getId());
        return Response.onSuccess(response);
    }

    @GetMapping("/single/read")
    public Response<List<MessageResponse>> findReadSingleMessages(
            @RequestParam("singleChatId") final Long singleChatId,
            @RequestParam("criteriaNumber") final long criteriaNumber
    ) {
        List<MessageResponse> responses = messageQueryService
                .findReadSingleMessages(singleChatId, criteriaNumber);
        return Response.onSuccess(responses);
    }

    @GetMapping("/single/unread")
    public Response<List<MessageResponse>> findUnreadSingleMessages(
            @RequestParam("singleChatId") final Long singleChatId,
            @RequestParam("criteriaNumber") final long criteriaNumber
    ) {
        List<MessageResponse> responses = messageQueryService
                .findUnreadSingleMessages(singleChatId, criteriaNumber);
        return Response.onSuccess(responses);
    }


    @GetMapping("/group/enter")
    public Response<EnterChatResponse> enterGroupChat(
            @RequestParam("groupChatId") final Long groupChatId,
            @AuthMember final Member member
    ) {
        EnterChatResponse response = messageQueryService
                .findDefaultGroupMessages(groupChatId, member.getId());
        return Response.onSuccess(response);
    }

    @GetMapping("/group/read")
    public Response<List<MessageResponse>> findReadGroupMessages(
            @RequestParam("groupChatId") final Long groupChatId,
            @RequestParam("criteriaNumber") final long criteriaNumber
    ) {
        List<MessageResponse> responses = messageQueryService
                .findReadGroupMessages(groupChatId, criteriaNumber);
        return Response.onSuccess(responses);
    }

    @GetMapping("/group/unread")
    public Response<List<MessageResponse>> findUnreadGroupMessages(
            @RequestParam("groupChatId") final Long groupChatId,
            @RequestParam("criteriaNumber") final long criteriaNumber
    ) {
        List<MessageResponse> responses = messageQueryService
                .findUnreadGroupMessages(groupChatId, criteriaNumber);
        return Response.onSuccess(responses);
    }
}
