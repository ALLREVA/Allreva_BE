package com.backend.allreva.chatting.chat.group_chat.command.domain;

import com.backend.allreva.chatting.chat.infra.MemberGroupChatDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface GroupChatRepository extends JpaRepository<GroupChat, Long>, MemberGroupChatDslRepository {

    @Query(
            "SELECT g.uuid " +
                    "FROM GroupChat g " +
                    "WHERE g.managerId = :memberId " +
                    "AND g.id = :groupChatId")
    UUID findGroupChatUuid(Long memberId, Long groupChatId);
}
