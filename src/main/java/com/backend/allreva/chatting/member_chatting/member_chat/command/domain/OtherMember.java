package com.backend.allreva.chatting.member_chatting.member_chat.command.domain;

import com.backend.allreva.common.model.Image;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OtherMember {

    private Long otherMemberId;
    private String otherMemberNickname;

    @Embedded
    private Image otherMemberThumbnail;

    public OtherMember(
            final Long otherMemberId,
            final String otherMemberNickname,
            final Image otherMemberThumbnail
    ) {
        this.otherMemberId = otherMemberId;
        this.otherMemberNickname = otherMemberNickname;
        this.otherMemberThumbnail = otherMemberThumbnail;
    }
}
