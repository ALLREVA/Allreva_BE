package com.backend.allreva.chatting.chat.group.command.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface MemberGroupChatRepository extends JpaRepository<MemberGroupChat, Long> {

    void deleteAllByGroupChatIdAndMemberId(Long groupChatId, Long memberId);

    @Query("SELECT m.memberId " +
            "FROM MemberGroupChat m " +
            "WHERE m.groupChatId = :groupChatId")
    Set<Long> findAllMemberIdByGroupChatId(Long groupChatId);
}
