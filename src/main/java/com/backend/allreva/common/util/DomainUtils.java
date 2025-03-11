package com.backend.allreva.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class DomainUtils {

    public static String getDomainName(HttpServletRequest request) {
        //  ️Nginx 프록시를 고려하여 먼저 `Host` 헤더 확인
        String domainName = request.getHeader("Host");
        log.info("Host: {}", domainName);

        // 만약 `Host` 헤더가 없으면 로컬 환경이므로 `request.getServerName()` 사용
        if (domainName == null || domainName.isEmpty()) {
            domainName = request.getServerName(); // 로컬에서는 "localhost"
            log.info("ServerName: {}", domainName);
        } else {
            domainName = domainName.split(":")[0]; // localhost 및 ip 경우 포트 번호 제거
        }

        return domainName;
    }
}
