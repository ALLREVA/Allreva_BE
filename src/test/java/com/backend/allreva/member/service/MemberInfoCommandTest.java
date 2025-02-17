package com.backend.allreva.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.backend.allreva.common.application.S3ImageService;
import com.backend.allreva.member.command.application.MemberInfoCommandService;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.command.domain.MemberRepository;
import com.backend.allreva.member.command.domain.value.MemberRole;
import com.backend.allreva.member.fixture.MemberFixture;
import com.backend.allreva.member.fixture.MemberRequestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberInfoCommandTest {

    @InjectMocks
    private MemberInfoCommandService memberInfoCommandService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private S3ImageService s3ImageService;

    Member member;

    @BeforeEach
    void setUp() {
        member = MemberFixture.createMemberFixture(1L, MemberRole.USER);
    }

    @Test
    void 회원_가입_시_회원_정보를_성공적으로_등록한다() {
        // given
        var memberRegisterRequest = MemberRequestFixture.createMemberRegisterRequest();
        given(memberRepository.save(any(Member.class))).willReturn(member);

        // when
        var updatedMember = memberInfoCommandService.registerMember(memberRegisterRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(updatedMember.getMemberInfo().getIntroduce()).isEqualTo(memberRegisterRequest.introduce());
            softly.assertThat(updatedMember.getMemberRole()).isEqualTo(MemberRole.USER);
        });
    }

    @Test
    void 회원_정보를_성공적으로_수정한다() {
        // given
        var memberRegisterRequest = MemberRequestFixture.createMemberRegisterRequest();
        given(memberRepository.save(any(Member.class))).willReturn(member);

        // when
        Member updatedMember = memberInfoCommandService.updateMemberInfo(memberRegisterRequest, member);

        // then
        assertThat(updatedMember.getMemberInfo().getIntroduce()).isEqualTo(memberRegisterRequest.introduce());
    }
}
