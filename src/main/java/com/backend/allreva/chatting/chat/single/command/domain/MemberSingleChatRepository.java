package com.backend.allreva.chatting.chat.single.command.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

public interface MemberSingleChatRepository extends JpaRepository<MemberSingleChat, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM MemberSingleChat m " +
            "WHERE m.singleChatId = :singleChatId AND m.memberId = :memberId")
    void deleteBySingleChatIdAndMemberId(Long singleChatId, Long memberId);

    @Query("SELECT m FROM MemberSingleChat m " +
            "WHERE m.singleChatId = :singleChatId AND m.memberId = :memberId")
    Optional<MemberSingleChat> findBySingleChatIdAndMemberId(Long singleChatId, Long memberId);

    @Query("SELECT m.memberId " +
            "FROM MemberSingleChat m " +
            "WHERE m.singleChatId = :singleChatId")
    Set<Long> findMemberIdBySingleChatId(Long singleChatId);
}
