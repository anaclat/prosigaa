package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioService usuarioService;

    @BeforeEach
    void configurar() {
        usuarioService = new UsuarioService(usuarioRepository);
    }

    @Test
    void deveRejeitarCadastroComEmailForaDoDominioInstitucional() {
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cadastrar("Teste", "teste@gmail.com", "123456", null));
        verifyNoInteractions(usuarioRepository);
    }
}
