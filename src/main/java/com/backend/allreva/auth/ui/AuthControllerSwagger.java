package com.backend.allreva.auth.ui;

import com.backend.allreva.auth.application.dto.UserInfoResponse;
import com.backend.allreva.common.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "회원 인증 및 인가 API", description = "인증 및 인가를 처리합니다")
public interface AuthControllerSwagger {

    @Operation(summary = "kakao oauth2 인가코드 요청", description = "인가코드를 바탕으로 사용자를 인증합니다.\n"
            + "먼저 카카오 로그인을 한 후에 authorization code를 받아서 이를 파라미터로 넘겨야 합니다.")
    Response<UserInfoResponse> authKakaoLogin(
            String authorizationCode,
            HttpServletRequest request,
            HttpServletResponse response
    );

    @Operation(summary = "access token 재발급 요청", description = "refresh token을 이용하여 access token을 재발급합니다.")
    Response<Void> reissueToken(
            String refreshToken,
            HttpServletRequest request,
            HttpServletResponse response
    );

    @Operation(summary = "로그인 체크", description = "refresh token을 이용하여 access token을 재발급합니다.")
    Response<UserInfoResponse> loginCheck(
            String refreshToken,
            HttpServletRequest request,
            HttpServletResponse response
    );
}
