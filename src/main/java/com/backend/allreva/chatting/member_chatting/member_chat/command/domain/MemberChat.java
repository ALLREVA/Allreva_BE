package com.backend.allreva.chatting.member_chatting.member_chat.command.domain;

import com.backend.allreva.common.model.Image;
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

    private Boolean enableNotification;

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
            final Long otherMemberId,
            final String otherMemberNickname,
            final String otherMemberThumbnail
    ) {
        this.memberId = memberId;
        this.chatId = chatId;
        this.enableNotification = true;

        this.otherMember = new OtherMember(
                otherMemberId,
                otherMemberNickname,
                new Image(otherMemberThumbnail)
        );
    }
}
