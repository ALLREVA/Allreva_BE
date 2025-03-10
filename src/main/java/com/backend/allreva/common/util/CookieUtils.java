package com.backend.allreva.common.util;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CookieUtils {

    // 쿠키 생성
    public static void addCookie(
            final HttpServletResponse response,
            final String cookieDomain,
            final String name,
            final String value,
            final int maxAge
    ) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .domain(cookieDomain)
                .path("/")
                .maxAge(maxAge)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
