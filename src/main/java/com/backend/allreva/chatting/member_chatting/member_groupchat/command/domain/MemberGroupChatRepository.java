package com.backend.allreva.chatting.member_chatting.member_groupchat.command.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGroupChatRepository extends JpaRepository<MemberGroupChat, Long> {

    void deleteAllByGroupChatIdAndMemberId(Long groupChatId, Long memberId);
}
