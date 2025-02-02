package com.backend.allreva.chatting.chat.group.command.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"group_chat_id", "member_id"})
})
@Entity
public class MemberGroupChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long groupChatId;
    private Long memberId;

    public MemberGroupChat(
            final Long memberId,
            final Long groupChatId
    ) {
        this.memberId = memberId;
        this.groupChatId = groupChatId;
    }
}
