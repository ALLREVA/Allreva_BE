package com.backend.allreva.chatting.chat.single.command.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(columnList = "member_id"),
        @Index(columnList = "chat_id")
})
@Entity
public class MemberChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;
    private Long memberId;

    @Embedded
    private OtherMember otherMember;

    /**
     * currentMessageId - lastMessageId 를 뺄셈해야함
     * mongoDB 가 가지고 있느냐
     */
    private String lastMessageId;

    public MemberChat(
            final Long memberId,
            final Long chatId,
            final OtherMember otherMember
    ) {
        this.memberId = memberId;
        this.chatId = chatId;

        this.otherMember = otherMember;
    }
}
