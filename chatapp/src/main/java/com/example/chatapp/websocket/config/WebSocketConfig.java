package com.example.chatapp.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple in-memory broker
        config.enableSimpleBroker("/topic", "/queue"); // destination prefix add queue for private
        config.setApplicationDestinationPrefixes("/app"); // where client will send messages
        config.setUserDestinationPrefix("/user");   // Key for private chat
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Client connects to this endpoint
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*").withSockJS();
    }
}

