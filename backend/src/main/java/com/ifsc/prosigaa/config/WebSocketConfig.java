package com.ifsc.prosigaa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${app.cors.allowed-origin-patterns:http://localhost:*,http://127.0.0.1:*,https://localhost:*,https://*.vercel.app,null,file://*}")
    private String allowedOriginPatterns;

    /**
     * Configura o broker de mensagens.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        /*
         * Mensagens enviadas pelo cliente para o servidor:
         *
         * /app/...
         *
         * Por exemplo:
         * /app/chat
         */
        registry.setApplicationDestinationPrefixes("/app");

        /*
         * Destinos usados pelo broker para entregar mensagens
         * aos clientes.
         */
        registry.enableSimpleBroker(
                "/topic",
                "/queue"
        );

    }

    /**
     * Endpoint que o frontend utiliza para estabelecer
     * a conexão WebSocket/STOMP.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns(parseAllowedOriginPatterns())
                .withSockJS();
    }

        private String[] parseAllowedOriginPatterns() {
        return Arrays.stream(allowedOriginPatterns.split(","))
            .map(String::trim)
            .filter(pattern -> !pattern.isEmpty())
            .toArray(String[]::new);
        }
}
