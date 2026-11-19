package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.enums.TipoPostagem;
import com.ifsc.prosigaa.enums.TipoUsuario;
import com.ifsc.prosigaa.model.Fase;
import com.ifsc.prosigaa.model.Materia;
import com.ifsc.prosigaa.model.Postagem;
import com.ifsc.prosigaa.model.Usuario;
import com.ifsc.prosigaa.repository.AnexoPostagemRepository;
import com.ifsc.prosigaa.repository.PostagemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostagemServiceTest {
    @Mock
    private PostagemRepository postagemRepository;

    @Mock
    private AnexoPostagemRepository anexoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private FaseService faseService;

    @Mock
    private MateriaService materiaService;

    @Mock
    private ArquivoPostagemStorage arquivoStorage;

    private PostagemService postagemService;

    @BeforeEach
    void configurar() {
        postagemService = new PostagemService(
                postagemRepository, anexoRepository, usuarioService, faseService, materiaService, arquivoStorage
        );
    }

    @Test
    void deveRecusarExclusaoDePostagemPorUsuarioDiferenteDoAutor() {
        Usuario autor = new Usuario("Autor", "autor@aluno.ifsc.edu.br", "hash", TipoUsuario.ALUNO);
        ReflectionTestUtils.setField(autor, "id", 10L);

        Fase fase = new Fase("Fase 1", 1);
        Materia materia = new Materia("Matemática", "MAT");
        Postagem postagem = new Postagem("Título", "Conteúdo", null, TipoPostagem.DUVIDA, autor, fase, materia);
        ReflectionTestUtils.setField(postagem, "id", 20L);

        when(postagemRepository.findById(20L)).thenReturn(Optional.of(postagem));

        assertThrows(IllegalArgumentException.class, () -> postagemService.excluir(20L, 99L));
        verify(postagemRepository).findById(20L);
    }

    @Test
    void deveOcultarNomeDoAutorEmPostagemAnonima() {
        Usuario autor = new Usuario("Autor", "autor@aluno.ifsc.edu.br", "hash", TipoUsuario.ALUNO);
        Fase fase = new Fase("Fase 1", 1);
        Materia materia = new Materia("Matemática", "MAT");
        Postagem postagem = new Postagem(
                "Título", "Conteúdo", null, TipoPostagem.DUVIDA, autor, fase, materia, true
        );

        var dto = postagemService.toDTO(postagem);
        assertEquals("Anônimo", dto.usuario().nome());
        assertTrue(dto.anonima());
    }
}
