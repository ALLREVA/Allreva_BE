package com.backend.allreva.chatting.member_chatting;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {

    private Long memberId;

    private String nickname;
    private String profileImageUrl;

    public Participant(
            final Long memberId,
            final String nickname,
            final String profileImageUrl
    ) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
