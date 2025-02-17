package com.backend.allreva.member.command.application.request;

import com.backend.allreva.common.model.Email;
import com.backend.allreva.common.model.Image;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.command.domain.value.LoginProvider;
import com.backend.allreva.member.command.domain.value.MemberRole;
import lombok.Builder;

import java.util.List;

@Builder
public record MemberRegisterRequest(
        String email,
        String nickname,
        String introduce,
        LoginProvider loginProvider,
        List<MemberArtistRequest> memberArtistRequests,
        Image image
) {

    public Member toEntity() {
        return Member.builder()
                .email(Email.builder()
                        .email(email)
                        .build())
                .nickname(nickname)
                .memberRole(MemberRole.USER)
                .introduce(introduce)
                .profileImageUrl(image.getUrl())
                .loginProvider(loginProvider)
                .build();
    }
}
