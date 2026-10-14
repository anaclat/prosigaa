package com.ifsc.prosigaa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class NotificacaoWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/notify")
    public ResponseEntity<Map<String, Object>> enviarNotificacaoTeste(
            @RequestParam(required = false) Long userId,
            @RequestBody(required = false) Map<String, Object> payload) {

        String mensagem = payload == null
                ? "Notificação de teste"
                : String.valueOf(payload.getOrDefault("mensagem", "Notificação de teste"));

        Map<String, Object> notificacao = new HashMap<>();
        notificacao.put("mensagem", mensagem);
        notificacao.put("tipo", "TESTE");
        notificacao.put("timestamp", LocalDateTime.now().toString());

        if (userId != null) {
            messagingTemplate.convertAndSend("/topic/usuarios/" + userId + "/notifications", notificacao);
        } else {
            messagingTemplate.convertAndSend("/topic/notifications", notificacao);
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", "enviado");
        resposta.put("mensagem", mensagem);
        return ResponseEntity.ok(resposta);
    }
}
