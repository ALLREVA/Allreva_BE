package com.backend.allreva.chatting.chat.single.command.domain;

import com.backend.allreva.common.model.Image;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OtherMember {

    private Long id;
    private String nickname;

    private Image thumbnail;

    public OtherMember(
            final Long id,
            final String nickname,
            final String otherMemberThumbnailUrl
    ) {
        this(
                id,
                nickname,
                new Image(otherMemberThumbnailUrl)
        );
    }

    public OtherMember(
            final Long id,
            final String nickname,
            final Image thumbnail
    ) {
        this.id = id;
        this.nickname = nickname;
        this.thumbnail = thumbnail;
    }

    public OtherMember(final OtherMember otherMember) {
        this.id = otherMember.id;
        this.nickname = otherMember.nickname;
        this.thumbnail = otherMember.thumbnail;
    }




}
