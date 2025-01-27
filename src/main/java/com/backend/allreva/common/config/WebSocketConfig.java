package com.backend.allreva.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static final String WEB_SOCKET_ENDPOINT = "/ws-chat";
    public static final String TOPIC = "/room";

    public static final String PREFIX = "/chat";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WEB_SOCKET_ENDPOINT)
                .setAllowedOriginPatterns("*") // 배포 전 수정 필요
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(TOPIC);
        registry.setApplicationDestinationPrefixes(PREFIX);
    }
}
