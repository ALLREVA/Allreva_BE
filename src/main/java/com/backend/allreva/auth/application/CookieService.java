package com.backend.allreva.auth.application;

import com.backend.allreva.common.util.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Value("${jwt.refresh.expiration}")
    private int refreshTime;
    @Value("${url.front.domain-name}")
    private String domainName;

    public void addRefreshTokenCookie(
            final HttpServletResponse response,
            final String refreshToken
    ) {
        CookieUtils.addCookie(
                response,
                domainName,
                "refreshToken",
                refreshToken,
                refreshTime
        );
    }
}
