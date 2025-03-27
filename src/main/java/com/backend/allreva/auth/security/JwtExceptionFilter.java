package com.backend.allreva.auth.security;

import com.backend.allreva.common.exception.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver resolver;

    public JwtExceptionFilter(
            @Qualifier("handlerExceptionResolver") final HandlerExceptionResolver resolver
    ) {
        this.resolver = resolver;
    }

    /**
     * 필터 처리 중 CustomException이 발생하면 해당 필터에서 처리합니다.
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            resolver.resolveException(request, response, null, e);
        }
    }
}
