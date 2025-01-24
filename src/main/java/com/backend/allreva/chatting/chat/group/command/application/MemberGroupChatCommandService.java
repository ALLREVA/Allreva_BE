package com.backend.allreva.chatting.chat.group.command.application;

import com.backend.allreva.chatting.chat.group.command.domain.GroupChatRepository;
import com.backend.allreva.chatting.chat.group.command.domain.MemberGroupChat;
import com.backend.allreva.chatting.chat.group.command.domain.MemberGroupChatRepository;
import com.backend.allreva.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberGroupChatCommandService {

    private final GroupChatRepository groupChatRepository;
    private final MemberGroupChatRepository memberGroupChatRepository;

    @Transactional
    public Long add(
            final String uuid,
            final Long memberId
    ) {
        Long groupChatId = groupChatRepository.findGroupChatIdByUuid(UUID.fromString(uuid))
                .orElseThrow(NotFoundException::new);

        MemberGroupChat memberGroupChat = new MemberGroupChat(memberId, groupChatId);
        return memberGroupChatRepository
                .save(memberGroupChat)
                .getId();
    }

    @Transactional
    public void leave(Long groupChatId, Long memberId) {
        memberGroupChatRepository
                .deleteAllByGroupChatIdAndMemberId(groupChatId, memberId);
    }
}
