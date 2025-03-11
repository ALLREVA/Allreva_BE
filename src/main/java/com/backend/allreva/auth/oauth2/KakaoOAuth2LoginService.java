package com.backend.allreva.auth.oauth2;

import com.backend.allreva.auth.application.OAuth2LoginService;
import com.backend.allreva.auth.application.dto.UserInfo;
import com.backend.allreva.auth.exception.code.InvalidRedirectUrlException;
import com.backend.allreva.member.command.domain.value.LoginProvider;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOAuth2LoginService implements OAuth2LoginService {

    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoUserInfoClient kakaoUserInfoClient;

    @Value("${url.front.domain-name}")
    private String prodDomainName;
    @Value("${oauth2.kakao.local-redirect-uri}")
    private String kakaoLocalRedirectUri;
    @Value("${oauth2.kakao.redirect-uri}")
    private String kakaoRedirectUri;
    @Value("${oauth2.kakao.client-id}")
    private String kakaoClientId;
    @Value("${oauth2.kakao.client-secret}")
    private String kakaoClientSecret;

    /**
     * 카카오 로그인 시 사용자 정보를 가져옵니다.
     * @param authorizationCode 인가 코드
     * @return 사용자 정보
     */
    @Override
    public UserInfo getUserInfo(
            final String authorizationCode,
            final String domainName
    ) {
        log.info("domainName: {}", domainName);
        log.info("prodDomainName: {}", prodDomainName);
        String redirectUri = getRedirectUri(domainName); // localhost or prod
        KakaoToken token = kakaoAuthClient.getToken(
                kakaoClientId,
                redirectUri,
                authorizationCode,
                "authorization_code",
                kakaoClientSecret
        );

        KakaoUserInfo kakaoUserInfo = kakaoUserInfoClient.getUserInfo(
                "Bearer " + token.accessToken()
        );

        return UserInfo.builder()
                .loginProvider(LoginProvider.KAKAO)
                .providerId(kakaoUserInfo.id())
                .email(kakaoUserInfo.kakaoAccount().email())
                .nickname(kakaoUserInfo.kakaoAccount().profile().nickname())
                .profileImageUrl(kakaoUserInfo.kakaoAccount().profile().profileImageUrl())
                .build();
    }

    private String getRedirectUri(final String domainName) {
        if (Objects.equals("localhost", domainName)) {
            return kakaoLocalRedirectUri;
        }
        if (Objects.equals(prodDomainName, domainName)) {
            return kakaoRedirectUri;
        }
        throw new InvalidRedirectUrlException();
    }
}
