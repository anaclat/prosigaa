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
// REST usa requisição-resposta; WebSocket mantém conexão persistente para tráfego full-duplex.
// Diferente de SSE (unidirecional servidor -> cliente), WebSocket permite envio nos dois sentidos;
// polling simula atualizações repetindo requisições e tende a gerar mais tráfego.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${app.cors.allowed-origin-patterns:*}")
    private String allowedOriginPatterns;

    // STOMP organiza mensagens em destinos: /app recebe ações da aplicação e /topic ou /queue
    // são destinos distribuídos pelo broker aos clientes inscritos.
    
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

    // /ws inicia a conexão STOMP; SockJS pode oferecer transportes alternativos quando
    // WebSocket direto não estiver disponível. CORS/origens continuam sendo uma decisão de segurança.
    
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
