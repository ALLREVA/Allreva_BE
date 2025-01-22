package com.backend.allreva.chatting.member_chatting.member_chat.command.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface MemberChatRepository extends JpaRepository<MemberChat, Long> {

    @Modifying
    @Transactional
    void deleteMemberChatByChatIdAndMemberId(Long singleChatId, Long memberId);
}
