package com.backend.allreva.chatting.chat.query;

import com.backend.allreva.chatting.chat.group_chat.command.domain.GroupChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GroupChatQueryService {

    private final GroupChatRepository groupChatRepository;

    public RoomInfoDetail findGroupChatInfo(
            final Long memberId,
            final Long groupChatId
    ) {
        return groupChatRepository.findGroupChatInfo(memberId, groupChatId);
    }

    public String findInviteCode(
            final Long memberId,
            final Long groupChatId
    ) {
        UUID uuid = groupChatRepository.findGroupChatUuid(memberId, groupChatId);
        return uuid.toString();
    }
}
