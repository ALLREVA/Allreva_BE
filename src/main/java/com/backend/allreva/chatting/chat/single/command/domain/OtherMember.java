package com.backend.allreva.chatting.chat.single.command.domain;

import com.backend.allreva.common.model.Image;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OtherMember {

    private Long otherMemberId;
    private String otherMemberNickname;

    private Image otherMemberThumbnail;

    public OtherMember(
            final Long otherMemberId,
            final String otherMemberNickname,
            final String otherMemberThumbnailUrl
    ) {
        this(
                otherMemberId,
                otherMemberNickname,
                new Image(otherMemberThumbnailUrl)
        );
    }

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
