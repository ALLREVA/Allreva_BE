package com.backend.allreva.auth.application;

import com.backend.allreva.auth.application.dto.UserInfo;
import com.backend.allreva.auth.application.dto.UserInfoResponse;
import com.backend.allreva.auth.exception.code.TokenEmptyException;
import com.backend.allreva.common.model.Email;
import com.backend.allreva.member.command.domain.Member;
import com.backend.allreva.member.command.domain.MemberRepository;
import com.backend.allreva.member.command.domain.value.LoginProvider;
import com.backend.allreva.member.exception.MemberNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final OAuth2LoginService oAuth2LoginService;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    /**
     * 카카오 로그인을 검증합니다.
     * @param authorizationCode 인가 코드
     * @return 로그인 응답
     */
    public UserInfoResponse kakaoLogin(
            final String authorizationCode,
            final String domainName
    ) {
        UserInfo userInfo = oAuth2LoginService.getUserInfo(authorizationCode, domainName);

        // 회원 존재 확인
        Email emailVO = Email.builder()
                .email(userInfo.email())
                .build();
        LoginProvider loginProvider = userInfo.loginProvider();
        Optional<Member> memberOptional = memberRepository.findByEmailAndLoginProvider(emailVO, loginProvider);

        if (memberOptional.isPresent()) {
            return getMemberInfo(memberOptional.get());
        }
        return getTemporaryMemberInfo(userInfo);

    }

    private UserInfoResponse getTemporaryMemberInfo(final UserInfo userInfo) {
        return UserInfoResponse.builder()
                .isUser(false)
                .email(userInfo.email())
                .nickname(userInfo.nickname())
                .profileImageUrl(userInfo.profileImageUrl())
                .build();
    }

    private UserInfoResponse getMemberInfo(final Member member) {
        // token 생성
        Long memberId = member.getId();
        String accessToken = jwtService.generateAccessToken(String.valueOf(memberId));
        String refreshToken = jwtService.generateRefreshToken(String.valueOf(memberId));

        // redis에 RefreshToken 저장
        jwtService.updateRefreshToken(refreshToken, memberId);

        return UserInfoResponse.builder()
                .isUser(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(member.getEmail().getEmail())
                .nickname(member.getMemberInfo().getNickname())
                .profileImageUrl(member.getMemberInfo().getProfileImageUrl())
                .build();
    }

    /**
     * Access Token을 재발급합니다.
     * @param refreshToken Refresh Token
     * @Return 재발급된 Access Token 및 Refresh Token
     */
    public UserInfoResponse reissueAccessToken(final String refreshToken) {
        // refresh token 검증
        if (refreshToken == null) {
            throw new TokenEmptyException();
        }
        jwtService.validateToken(refreshToken);
        jwtService.validRefreshTokenExistInRedis(refreshToken);

        String memberId = jwtService.extractMemberId(refreshToken);

        // member db 확인
        Member member = memberRepository.findById(Long.valueOf(memberId))
                .orElseThrow(MemberNotFoundException::new);

        // access token 재발급
        String generatedAccessToken = jwtService.generateAccessToken(memberId);

        // token rotate
        String generateRefreshToken = jwtService.generateRefreshToken(memberId);
        jwtService.updateRefreshToken(generateRefreshToken, Long.valueOf(memberId));

        return UserInfoResponse.builder()
                .isUser(true)
                .accessToken(generatedAccessToken)
                .refreshToken(generateRefreshToken)
                .email(member.getEmail().getEmail())
                .nickname(member.getMemberInfo().getNickname())
                .profileImageUrl(member.getMemberInfo().getProfileImageUrl())
                .build();
    }
}
