package com.backend.allreva.common.config;

import static com.backend.allreva.common.config.SecurityEndpointPaths.ADMIN_LIST;
import static com.backend.allreva.common.config.SecurityEndpointPaths.ANONYMOUS_LIST;
import static com.backend.allreva.common.config.SecurityEndpointPaths.USER_LIST;
import static com.backend.allreva.common.config.SecurityEndpointPaths.WHITE_LIST;

import com.backend.allreva.auth.security.CustomAccessDeniedHandler;
import com.backend.allreva.auth.security.CustomAuthenticationEntryPoint;
import com.backend.allreva.auth.security.JwtAuthenticationFilter;
import com.backend.allreva.auth.security.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // CORS
    private final CorsConfigurationSource corsConfigurationSource;

    // JWT
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                .cors(it -> it.configurationSource(corsConfigurationSource))
                .csrf(CsrfConfigurer<HttpSecurity>::disable)
                .formLogin(FormLoginConfigurer<HttpSecurity>::disable)
                .httpBasic(HttpBasicConfigurer<HttpSecurity>::disable)
                .headers(it -> it.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .sessionManagement(it -> it.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers(ANONYMOUS_LIST).permitAll()
                        .requestMatchers(USER_LIST).hasRole("USER")
                        .requestMatchers(ADMIN_LIST).hasRole("ADMIN")
                        .anyRequest().authenticated());

        // jwt 인증 필터
        http
                .addFilterAfter(jwtAuthenticationFilter, LogoutFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
