package com.example.backend.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.example.backend.seciurity.KeycloakJwtTokenConverter;


@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer 
{
    @Autowired
    private JwtDecoder jwtDecoder; // Wstrzykujemy JwtDecoder

    @Autowired
    private KeycloakJwtTokenConverter keycloakJwtTokenConverter;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // Sprawdzamy, czy to jest ramka CONNECT
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    
                    // Szukamy nagłówka Authorization
                    String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
                    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                        String token = authorizationHeader.substring(7);
                        try {
                            // Dekodujemy i walidujemy token
                            Jwt jwt = jwtDecoder.decode(token);
                            // Konwertujemy JWT na obiekt Authentication
                            Authentication authentication = keycloakJwtTokenConverter.convert(jwt);
                            // Ustawiamy uwierzytelnionego użytkownika dla tej sesji WebSocket
                            accessor.setUser(authentication);
                        } catch (Exception e) {
                            // Błąd dekodowania tokena - użytkownik pozostaje nieuwierzytelniony
                        }
                    }
                }
                return message;
            }
        });
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-game")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
