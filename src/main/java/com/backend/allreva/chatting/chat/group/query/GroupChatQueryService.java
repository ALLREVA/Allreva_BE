package com.backend.allreva.chatting.chat.group.query;

import com.backend.allreva.chatting.chat.group.command.domain.GroupChat;
import com.backend.allreva.chatting.chat.group.command.domain.GroupChatRepository;
import com.backend.allreva.chatting.chat.group.query.response.GroupChatDetailResponse;
import com.backend.allreva.chatting.chat.group.query.response.GroupChatOverviewResponse;
import com.backend.allreva.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GroupChatQueryService {

    private final GroupChatRepository groupChatRepository;

    public GroupChatDetailResponse findGroupChatInfo(
            final Long memberId,
            final Long groupChatId
    ) {
        return groupChatRepository.findGroupChatDetail(memberId, groupChatId)
                .orElseThrow(NotFoundException::new);
    }

    public GroupChatOverviewResponse findOverview(
            final String uuid
    ) {
        return groupChatRepository.findGroupChatOverview(UUID.fromString(uuid))
                .orElseThrow(NotFoundException::new);
    }

    public String findInviteCode(
            final Long memberId,
            final Long groupChatId
    ) {
        UUID uuid = groupChatRepository.findGroupChatUuid(memberId, groupChatId);
        return uuid.toString();
    }
}
