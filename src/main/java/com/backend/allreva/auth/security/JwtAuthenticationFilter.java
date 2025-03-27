package com.backend.allreva.auth.security;

import com.backend.allreva.auth.application.JwtService;
import com.backend.allreva.common.config.SecurityEndpointPaths;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * JWT 토큰을 검증하고 권한을 부여합니다.
     */
    @Override
    protected void doFilterInternal(
            @NonNull final HttpServletRequest request,
            @NonNull final HttpServletResponse response,
            @NonNull final FilterChain filterChain
    ) throws ServletException, IOException {
        // white list 요청 처리
        PathMatcher pathMatcher = new AntPathMatcher();
        boolean isWhiteListed = Arrays.stream(SecurityEndpointPaths.WHITE_LIST)
                .anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()));
        if (isWhiteListed) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtService.extractAccessToken(request);

        // ANONYMOUS 요청 처리
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Access Token X
        jwtService.validateToken(accessToken);

        // Access Token O
        String memberId = jwtService.extractMemberId(accessToken);

        setAuthenication(memberId, request);
        filterChain.doFilter(request, response);
    }

    /**
     * 사용자 인증을 수행하고 SecurityContextHolder에 저장합니다.
     * @param memberId 사용자 ID
     * @param request  HTTP 요청 객체
     */
    private void setAuthenication(final String memberId, final HttpServletRequest request) {
        // member db 확인
        UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        // add info (default - remote ip address, session id)
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // Context Holder 저장
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
