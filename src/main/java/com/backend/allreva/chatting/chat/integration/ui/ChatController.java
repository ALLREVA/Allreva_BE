package com.backend.allreva.chatting.chat.integration.ui;

import com.backend.allreva.auth.security.AuthMember;
import com.backend.allreva.chatting.chat.integration.model.ChatParticipantDoc;
import com.backend.allreva.chatting.chat.integration.model.ChatParticipantRepository;
import com.backend.allreva.chatting.chat.integration.model.value.ChatSummary;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.SortedSet;

@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@RestController
public class ChatController {

    private final ChatParticipantRepository chatParticipantRepository;


    @GetMapping("/list")
    public Response<SortedSet<ChatSummary>> findParticipatingRooms(
            @AuthMember final Member member
    ) {
        ChatParticipantDoc document = chatParticipantRepository.findById(member.getId())
                .orElseThrow(MemberNotFoundException::new);

        SortedSet<ChatSummary> responses = document.getChatSummaries();
        return Response.onSuccess(responses);
    }
}
