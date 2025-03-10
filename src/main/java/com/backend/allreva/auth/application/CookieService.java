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
    private String prodDomainName;

    public void addRefreshTokenCookie(
            final HttpServletResponse response,
            final String refreshToken,
            final String domainName
    ) {
        CookieUtils.addCookie(
                response,
                isLocalhost(domainName) ? null : prodDomainName,
                "refreshToken",
                refreshToken,
                refreshTime
        );
    }

    private static boolean isLocalhost(String domain) {
        return domain.equals("localhost");
    }
}
