package com.backend.allreva.member.fixture;

import com.backend.allreva.common.model.Image;
import com.backend.allreva.member.command.application.request.MemberArtistRequest;
import com.backend.allreva.member.command.application.request.MemberRegisterRequest;
import com.backend.allreva.member.command.application.request.RefundAccountRequest;
import com.backend.allreva.member.command.domain.value.LoginProvider;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class MemberRequestFixture {

    public static MemberRegisterRequest createMemberRegisterRequest() {
        return MemberRegisterRequest.builder()
                .email("my@email")
                .nickname("nickname")
                .loginProvider(LoginProvider.GOOGLE)
                .introduce("introduce")
                .memberArtistRequests(List.of(new MemberArtistRequest("spotify_1L","name1")))
                .image(new Image("https://my_picture"))
                .build();
    }

    public static RefundAccountRequest createRefundAccountRequest() {
        return new RefundAccountRequest("땡땡은행", "123456789");
    }
}
