package com.backend.allreva.chatting.chat.group.command.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGroupChatRepository extends JpaRepository<MemberGroupChat, Long> {

    void deleteAllByGroupChatIdAndMemberId(Long groupChatId, Long memberId);
}
