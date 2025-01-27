package com.backend.allreva.chatting.chat.group.command.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberGroupChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private Long groupChatId;

    public MemberGroupChat(
            final Long memberId, final Long groupChatId
    ) {
        this.memberId = memberId;
        this.groupChatId = groupChatId;
    }
}
