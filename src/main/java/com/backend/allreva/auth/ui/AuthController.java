package com.backend.allreva.auth.ui;

import com.backend.allreva.auth.application.AuthService;
import com.backend.allreva.auth.application.CookieService;
import com.backend.allreva.auth.application.dto.UserInfoResponse;
import com.backend.allreva.common.dto.Response;
import com.backend.allreva.common.util.DomainUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerSwagger {

    private final AuthService authService;
    private final CookieService cookieService;

    @GetMapping("/token/kakao")
    public Response<UserInfoResponse> authKakaoLogin(
            @RequestParam("code") final String authorizationCode,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        String domainName = DomainUtils.getDomainName(request);
        UserInfoResponse userInfoResponse = authService.kakaoLogin(authorizationCode);
        if (userInfoResponse.isUser()) {
            cookieService.addRefreshTokenCookie(response, userInfoResponse.refreshToken(), domainName);
            response.addHeader("Authorization", "Bearer " + userInfoResponse.accessToken());
        }
        return Response.onSuccess(userInfoResponse);
    }

    @GetMapping("/token/reissue")
    public Response<Void> reissueToken(
            @CookieValue(name = "refreshToken", required = false) final String refreshToken,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        String domainName = DomainUtils.getDomainName(request);
        UserInfoResponse userInfoResponse = authService.reissueAccessToken(refreshToken);
        cookieService.addRefreshTokenCookie(response, userInfoResponse.refreshToken(), domainName);
        response.addHeader("Authorization", "Bearer " + userInfoResponse.accessToken());
        return Response.onSuccess();
    }

    @GetMapping("/login/check")
    public Response<UserInfoResponse> loginCheck(
            @CookieValue(name = "refreshToken", required = false) final String refreshToken,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        String domainName = DomainUtils.getDomainName(request);
        UserInfoResponse userInfoResponse = authService.reissueAccessToken(refreshToken);
        cookieService.addRefreshTokenCookie(response, userInfoResponse.refreshToken(), domainName);
        response.addHeader("Authorization", "Bearer " + userInfoResponse.accessToken());
        return Response.onSuccess(userInfoResponse);
    }
}
