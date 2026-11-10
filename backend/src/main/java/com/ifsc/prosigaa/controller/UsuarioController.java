package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.dto.*;
import com.ifsc.prosigaa.service.UsuarioService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Cadastro e autenticação de usuários")
// Este controller expõe leitura e criação (parte de CRUD) e a ação de login; não há update/delete.
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    // GET é seguro e idempotente. A listagem retorna somente dados públicos, nunca a senha.
    // REST é stateless: a chamada não depende de uma sessão HTTP previamente criada.
    @Operation(summary = "Listar usuários", description = "Retorna dados públicos, sem senha ou hash.")
    @ApiResponse(responseCode = "200", description = "Lista de usuários")
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public List<UsuarioResumoDTO> listar() {
        return usuarioService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // POST cadastra uma nova conta, portanto não oferece idempotência automática.
    // O 201 confirma a criação; 409 representa e-mail já existente.
    @Operation(summary = "Cadastrar usuário", description = "Cria uma conta e armazena a senha com BCrypt.")
    @ApiResponse(responseCode = "201", description = "Usuário criado")
    @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "409", description = "E-mail já cadastrado", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public UsuarioDTO cadastrar(@RequestBody CadastroUsuarioDTO dto) {
        return usuarioService.cadastrar(dto.nome(), dto.email(), dto.senha(), dto.tipo());
    }

    @PostMapping("/login")
    // POST envia credenciais no corpo, evitando colocá-las na URL; repetição não cria recurso.
    // Este protótipo retorna o usuário, mas não cria sessão nem token: chamadas futuras não herdam o login.
    // Statelessness não substitui autenticação: confiar em usuarioId enviado pelo cliente não prova identidade.
    @Operation(summary = "Autenticar usuário", description = "Valida e-mail e senha e retorna dados públicos.")
    @ApiResponse(responseCode = "200", description = "Autenticação realizada")
    @ApiResponse(responseCode = "400", description = "Credenciais inválidas", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public UsuarioDTO login(@RequestBody LoginDTO dto) {
        return usuarioService.login(dto.email(), dto.senha());
    }
}
