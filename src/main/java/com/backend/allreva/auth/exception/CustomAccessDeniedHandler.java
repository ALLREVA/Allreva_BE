package com.backend.allreva.auth.exception;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final HandlerExceptionResolver resolver;

    public CustomAccessDeniedHandler(
        @Qualifier("handlerExceptionResolver") final HandlerExceptionResolver resolver
    ) {
        this.resolver = resolver;
    }

    /**
     * 유효하지 않는 권한일 때 호출되는 메서드
     */
    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Exception exception = (Exception) request.getAttribute("jakarta.servlet.error.exception");
        if (exception != null) {
            log.info("Access denied: {}", exception.getMessage());
            resolver.resolveException(request, response, null, exception);
        } else {
            resolver.resolveException(request, response, null, accessDeniedException);
        }
    }
}
