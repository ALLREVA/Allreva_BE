package com.backend.allreva.auth.application;

import com.backend.allreva.common.util.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
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

    public void deleteRefreshTokenCookie(
            final HttpServletResponse response,
            final String domainName
    ) {
        CookieUtils.deleteCookie(
                response,
                isLocalhost(domainName) ? null : prodDomainName,
                "refreshToken"
        );
    }

    private static boolean isLocalhost(String domain) {
        try {
            URL url = new URL(domain);
            String host = url.getHost();
            return host.contains("localhost") || host.contains("127.0.0.1");
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
