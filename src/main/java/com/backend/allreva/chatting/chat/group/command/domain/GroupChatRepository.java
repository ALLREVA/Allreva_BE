package com.backend.allreva.chatting.chat.group.command.domain;

import com.backend.allreva.chatting.chat.group.infra.MemberGroupChatDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface GroupChatRepository extends JpaRepository<GroupChat, Long>, MemberGroupChatDslRepository {

    @Query(
            "SELECT g.uuid " +
                    "FROM GroupChat g " +
                    "WHERE g.managerId = :memberId " +
                    "AND g.id = :groupChatId")
    UUID findGroupChatUuid(Long memberId, Long groupChatId);

    @Query(
            "SELECT g.id " +
                    "FROM GroupChat g " +
                    "WHERE g.uuid = :uuid"
    )
    Optional<Long> findGroupChatIdByUuid(UUID uuid);
}
