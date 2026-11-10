package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.exception.ConflictException;
import com.ifsc.prosigaa.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
// Centraliza exceções de todos os controllers e traduz falhas de domínio para status HTTP e JSON.
public class ApiExceptionHandler {
    // Um formato de erro consistente facilita clientes HTTP e também é descrito no OpenAPI.
    private Map<String, Object> corpo(HttpStatus status, String mensagem) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("status", status.value());
        corpo.put("erro", status.getReasonPhrase());
        corpo.put("mensagem", mensagem);
        corpo.put("timestamp", LocalDateTime.now().toString());
        return corpo;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> naoEncontrado(ResourceNotFoundException e) {
        // 404 Not Found: a rota existe, mas o recurso solicitado não foi encontrado.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpo(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> rotaNaoEncontrada(NoResourceFoundException e) {
        // 404 também é retornado quando o próprio caminho/endpoint não possui mapeamento.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpo(HttpStatus.NOT_FOUND, "Endpoint não encontrado."));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> conflito(ConflictException e) {
        // 409 Conflict: a operação conflita com o estado atual, por exemplo e-mail duplicado.
        return ResponseEntity.status(HttpStatus.CONFLICT).body(corpo(HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> requisicaoInvalida(IllegalArgumentException e) {
        // 400 Bad Request: os dados recebidos não atendem às regras esperadas.
        return ResponseEntity.badRequest().body(corpo(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<Map<String, Object>> payloadInvalido(Exception e) {
        // 400 cobre JSON malformado ou incompatível com o contrato de entrada.
        return ResponseEntity.badRequest().body(corpo(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou incompleto."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> integridade(DataIntegrityViolationException e) {
        // 409 distingue violação de restrição/conflito de um erro inesperado do servidor (500).
        return ResponseEntity.status(HttpStatus.CONFLICT).body(corpo(HttpStatus.CONFLICT, "A operação viola uma restrição de integridade dos dados."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> erroInterno(Exception e) {
        // 500 é a resposta genérica; detalhes internos não são expostos ao cliente.
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(corpo(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado."));
    }
}
