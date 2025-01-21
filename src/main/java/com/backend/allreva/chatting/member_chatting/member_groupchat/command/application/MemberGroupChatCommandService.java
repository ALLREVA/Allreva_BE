package com.backend.allreva.chatting.member_chatting.member_groupchat.command.application;

import com.backend.allreva.chatting.member_chatting.member_groupchat.command.domain.MemberGroupChat;
import com.backend.allreva.chatting.member_chatting.member_groupchat.command.domain.MemberGroupChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberGroupChatCommandService {

    private final MemberGroupChatRepository memberGroupChatRepository;

    public Long add(
            final Long groupChatId,
            final Long memberId
    ) {
        MemberGroupChat memberGroupChat = new MemberGroupChat(memberId, groupChatId);
        return memberGroupChatRepository
                .save(memberGroupChat)
                .getId();
    }

    public void leave(Long groupChatId, Long memberId) {
        memberGroupChatRepository
                .deleteAllByGroupChatIdAndMemberId(groupChatId, memberId);
    }
}
