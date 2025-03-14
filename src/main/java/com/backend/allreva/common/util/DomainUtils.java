package com.backend.allreva.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class DomainUtils {

    public static String getDomainName(HttpServletRequest request) {
        //  ️Nginx 프록시를 고려하여 `Origin` 헤더 확인
        String domainName = request.getHeader("Origin");
        if (domainName == null) {
            domainName = request.getHeader("Referer");
        }
        log.info("DomainName: {}", domainName);

        return domainName;
    }
}
