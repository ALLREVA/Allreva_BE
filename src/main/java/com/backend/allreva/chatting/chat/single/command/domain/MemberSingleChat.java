package com.backend.allreva.chatting.chat.single.command.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(columnList = "member_id"),
        @Index(columnList = "single_chat_id")
})
@Entity
public class MemberSingleChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long singleChatId;
    private Long memberId;

    @Embedded
    private OtherMember otherMember;

    /**
     * currentMessageId - lastMessageId 를 뺄셈해야함
     * mongoDB 가 가지고 있느냐
     */
    private String lastMessageId;

    public MemberSingleChat(
            final Long memberId,
            final Long singleChatId,
            final OtherMember otherMember
    ) {
        this.memberId = memberId;
        this.singleChatId = singleChatId;

        this.otherMember = otherMember;
    }
}
