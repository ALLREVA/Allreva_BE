package com.backend.allreva.chatting.chat.group.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.chat.group.command.application.GroupChatCommandService;
import com.backend.allreva.chatting.chat.group.command.application.request.DeleteGroupChatRequest;
import com.backend.allreva.chatting.chat.group.command.application.request.JoinGroupChatRequest;
import com.backend.allreva.chatting.chat.group.command.application.request.LeaveGroupChatRequest;
import com.backend.allreva.chatting.chat.group.command.application.request.UpdateGroupChatRequest;
import com.backend.allreva.chatting.chat.group.query.GroupChatQueryService;
import com.backend.allreva.chatting.chat.group.query.response.GroupChatDetailResponse;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/v1/chat/group")
@RestController
public class GroupChatController {

    private final GroupChatCommandService groupChatCommandService;
    private final GroupChatQueryService groupChatQueryService;

    @GetMapping("/{groupChatId}")
    public Response<GroupChatDetailResponse> findGroupChatInformation(
            @PathVariable("groupChatId") final Long groupChatId,
            @AuthMember final Member member
    ) {
        GroupChatDetailResponse response = groupChatQueryService.findGroupChatInfo(member.getId(), groupChatId);
        return Response.onSuccess(response);
    }

    @GetMapping("/invitation/{groupChatId}")
    public Response<String> findInviteUrl(
            @PathVariable("groupChatId") final Long groupChatId,
            @AuthMember final Member member
    ) {
        String inviteCode = groupChatQueryService
                .findInviteCode(member.getId(), groupChatId);
        return Response.onSuccess(inviteCode);
    }

    @PatchMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response<Void> updateGroupChat(
            @RequestPart("request") final UpdateGroupChatRequest request,
            @RequestPart(value = "imageFile", required = false) final MultipartFile imageFile,
            @AuthMember final Member member
    ) {
        groupChatCommandService.update(
                request,
                imageFile,
                member.getId()
        );
        return Response.onSuccess();
    }

    @PostMapping("/join")
    public Response<Long> joinGroupChat(
            @RequestBody final JoinGroupChatRequest request,
            @AuthMember final Member member
    ) {
        Long memberGroupChatId = groupChatCommandService
                .join(request.uuid(), member.getId());
        return Response.onSuccess(memberGroupChatId);
    }

    @DeleteMapping("/leave")
    public Response<Void> leaveGroupChat(
            @RequestBody final LeaveGroupChatRequest request,
            @AuthMember final Member member
    ) {
        groupChatCommandService
                .leave(request.groupChatId(), member.getId());
        return Response.onSuccess();
    }

    @DeleteMapping
    public Response<Void> deleteGroupChat(
            @RequestBody final DeleteGroupChatRequest request,
            @AuthMember final Member member
    ) {
        groupChatCommandService.delete(
                request.groupChatId(),
                member.getId()
        );
        return Response.onSuccess();
    }

}
