package com.ifsc.prosigaa.controller;

import com.ifsc.prosigaa.dto.CadastroUsuarioDTO;
import com.ifsc.prosigaa.dto.LoginDTO;
import com.ifsc.prosigaa.dto.UsuarioDTO;
import com.ifsc.prosigaa.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioDTO> listar() {
        return usuarioService.listar();
    }

    @PostMapping("/cadastro")
    public UsuarioDTO cadastrar(@RequestBody CadastroUsuarioDTO dto) {
        return usuarioService.cadastrar(dto.nome(), dto.email(), dto.senha(), dto.tipo());
    }

    @PostMapping("/login")
    public UsuarioDTO login(@RequestBody LoginDTO dto) {
        return usuarioService.login(dto.email(), dto.senha());
    }
}
