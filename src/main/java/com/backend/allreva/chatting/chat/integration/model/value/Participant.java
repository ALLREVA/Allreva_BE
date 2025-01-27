package com.backend.allreva.chatting.chat.integration.model.value;

import com.backend.allreva.common.model.Image;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {

    private Long memberId;

    private String nickname;
    private Image profileImage;

    public Participant(
            final Long memberId,
            final String nickname,
            final Image profileImage
    ) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
