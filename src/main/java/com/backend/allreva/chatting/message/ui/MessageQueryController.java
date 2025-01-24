package com.backend.allreva.chatting.message.ui;

import com.backend.allreva.chatting.message.query.MessageQueryService;
import com.backend.allreva.chatting.message.query.MessageResponse;
import com.backend.allreva.common.dto.Response;
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
    public Response<List<MessageResponse>> enterSingleChat(
            @RequestParam("singleChatId") final Long singleChatId,
            @RequestParam("lastReadMessageNumber") final long lastReadMessageNumber
    ) {
        List<MessageResponse> responses = messageQueryService
                .findDefaultSingleMessages(singleChatId, lastReadMessageNumber);
        return Response.onSuccess(responses);
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
    public Response<List<MessageResponse>> enterGroupChat(
            @RequestParam("groupChatId") final Long groupChatId,
            @RequestParam("lastReadMessageNumber") final long lastReadMessageNumber
    ) {
        List<MessageResponse> responses = messageQueryService
                .findDefaultGroupMessages(groupChatId, lastReadMessageNumber);
        return Response.onSuccess(responses);
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
