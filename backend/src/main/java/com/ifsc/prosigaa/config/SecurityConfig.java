package com.ifsc.prosigaa.config;

import com.ifsc.prosigaa.dto.UsuarioDTO;
import com.ifsc.prosigaa.service.UsuarioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            UsuarioService usuarioService,
            ObjectProvider<ClientRegistrationRepository> registrations,
            @Value("${app.oauth2.success-url:http://localhost:5173/oauth2/callback}") String successUrl
    ) throws Exception {
        http
                // A API existente usa autenticação própria no controller e não sessão Spring.
                // Desabilitar CSRF preserva os POSTs JSON atuais; OAuth mantém seu state via sessão.
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        // Sem credenciais configuradas, o backend continua iniciando normalmente e a API
        // tradicional permanece disponível; o fluxo OAuth é habilitado ao configurar Google.
        if (registrations.getIfAvailable() != null) {
            http.oauth2Login(oauth -> oauth.successHandler((request, response, authentication) -> {
                    var attributes = ((org.springframework.security.oauth2.core.user.OAuth2User)
                            authentication.getPrincipal()).getAttributes();
                    String email = stringAttribute(attributes, "email");
                    Object verified = attributes.get("email_verified");
                    if (email == null || !Boolean.TRUE.equals(verified)) {
                        response.sendRedirect(appendQuery(successUrl, "error", "email_google_nao_verificado"));
                        return;
                    }

                    try {
                        UsuarioDTO usuario = usuarioService.loginGoogle(stringAttribute(attributes, "name"), email);
                        String target = appendQuery(successUrl, "usuarioId", usuario.id().toString());
                        target = appendQuery(target, "usuarioNome", usuario.nome());
                        response.sendRedirect(target);
                    } catch (IllegalArgumentException exception) {
                        response.sendRedirect(appendQuery(successUrl, "error", "email_institucional_ifsc_obrigatorio"));
                    }
            }));
        }
        return http.build();
    }

    private static String stringAttribute(java.util.Map<String, Object> attributes, String name) {
        Object value = attributes.get(name);
        return value instanceof String string && !string.isBlank() ? string : null;
    }

    private static String appendQuery(String url, String key, String value) {
        String separator = url.contains("?") ? "&" : "?";
        return url + separator + key + "=" + UriUtils.encodeQueryParam(value, StandardCharsets.UTF_8);
    }
}
