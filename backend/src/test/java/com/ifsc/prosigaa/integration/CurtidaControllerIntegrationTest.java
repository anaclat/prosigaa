package com.ifsc.prosigaa.integration;

import com.ifsc.prosigaa.model.CurtidaComentario;
import com.ifsc.prosigaa.model.CurtidaPostagem;
import com.ifsc.prosigaa.model.Postagem;
import com.ifsc.prosigaa.model.Usuario;
import com.ifsc.prosigaa.repository.ComentarioRepository;
import com.ifsc.prosigaa.repository.CurtidaComentarioRepository;
import com.ifsc.prosigaa.repository.CurtidaPostagemRepository;
import com.ifsc.prosigaa.repository.PostagemRepository;
import com.ifsc.prosigaa.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:curtida-unlike-integration;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class CurtidaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private CurtidaPostagemRepository curtidaPostagemRepository;

    @Autowired
    private CurtidaComentarioRepository curtidaComentarioRepository;

    @Test
    void deveDescurtirPostagemPersistida() throws Exception {
        Usuario usuario = usuarioRepository.findByEmail("aluno@ifsc.edu.br").orElseThrow();
        Postagem postagem = postagemRepository.findAll().get(0);
        CurtidaPostagem curtida = curtidaPostagemRepository.save(new CurtidaPostagem(usuario, postagem));

        assertTrue(curtidaPostagemRepository.existsByUsuarioIdAndPostagemId(usuario.getId(), postagem.getId()));

        mockMvc.perform(delete(
                        "/curtidas/postagens/{postagemId}/usuarios/{usuarioId}",
                        postagem.getId(),
                        usuario.getId()
                ))
                .andExpect(status().isOk());

        assertFalse(curtidaPostagemRepository.existsById(curtida.getId()));
    }

    @Test
    void deveDescurtirComentarioPersistido() throws Exception {
        Usuario usuario = usuarioRepository.findByEmail("aluno@ifsc.edu.br").orElseThrow();
        var comentario = comentarioRepository.findAll().get(0);
        CurtidaComentario curtida = curtidaComentarioRepository.save(new CurtidaComentario(usuario, comentario));

        assertTrue(curtidaComentarioRepository.existsByUsuarioIdAndComentarioId(usuario.getId(), comentario.getId()));

        mockMvc.perform(delete(
                        "/curtidas/comentarios/{comentarioId}/usuarios/{usuarioId}",
                        comentario.getId(),
                        usuario.getId()
                ))
                .andExpect(status().isOk());

        assertFalse(curtidaComentarioRepository.existsById(curtida.getId()));
    }
}
