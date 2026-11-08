package com.ifsc.prosigaa.integration;

import com.ifsc.prosigaa.enums.TipoPostagem;
import com.ifsc.prosigaa.model.Comentario;
import com.ifsc.prosigaa.model.CurtidaComentario;
import com.ifsc.prosigaa.model.CurtidaPostagem;
import com.ifsc.prosigaa.model.Postagem;
import com.ifsc.prosigaa.repository.ComentarioRepository;
import com.ifsc.prosigaa.repository.CurtidaComentarioRepository;
import com.ifsc.prosigaa.repository.CurtidaPostagemRepository;
import com.ifsc.prosigaa.repository.PostagemRepository;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.jpa.properties.hibernate.generate_statistics=true")
class ConsultaPostagensBatchTest {
    private static final int QUANTIDADE_POSTAGENS_TESTE = 60;
    private static final int LIMITE_CONSULTAS = 40;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private CurtidaPostagemRepository curtidaPostagemRepository;

    @Autowired
    private CurtidaComentarioRepository curtidaComentarioRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    void deveCarregarFeedComAssociacoesEmLotes() throws Exception {
        Postagem modelo = postagemRepository.findAll().get(0);
        long postagensExistentes = postagemRepository.count();
        List<Postagem> postagensTeste = new ArrayList<>();
        List<Comentario> comentariosTeste = new ArrayList<>();
        List<CurtidaPostagem> curtidasPostagemTeste = new ArrayList<>();
        List<CurtidaComentario> curtidasComentarioTeste = new ArrayList<>();

        try {
            for (int i = 0; i < QUANTIDADE_POSTAGENS_TESTE; i++) {
                Postagem postagem = new Postagem(
                        "Postagem de teste " + i,
                        "Conteúdo de teste",
                        null,
                        TipoPostagem.DUVIDA,
                        modelo.getUsuario(),
                        modelo.getFase(),
                        modelo.getMateria()
                );
                postagensTeste.add(postagem);
            }
            postagemRepository.saveAll(postagensTeste);

            for (Postagem postagem : postagensTeste) {
                curtidasPostagemTeste.add(new CurtidaPostagem(modelo.getUsuario(), postagem));

                for (int i = 0; i < 2; i++) {
                    Comentario comentario = new Comentario("Comentário de teste", modelo.getUsuario(), postagem);
                    postagem.adicionarComentario(comentario);
                    comentariosTeste.add(comentario);
                }
            }
            comentarioRepository.saveAll(comentariosTeste);
            curtidaPostagemRepository.saveAll(curtidasPostagemTeste);

            for (Comentario comentario : comentariosTeste) {
                curtidasComentarioTeste.add(new CurtidaComentario(modelo.getUsuario(), comentario));
            }
            curtidaComentarioRepository.saveAll(curtidasComentarioTeste);

            Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
            statistics.clear();

            mockMvc.perform(get("/postagens"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(postagensExistentes + QUANTIDADE_POSTAGENS_TESTE));

            long consultasExecutadas = statistics.getPrepareStatementCount();
            assertTrue(
                    consultasExecutadas < LIMITE_CONSULTAS,
                    "Esperava menos de " + LIMITE_CONSULTAS
                            + " consultas para o feed; foram executadas " + consultasExecutadas
            );
        } finally {
            jdbcTemplate.update("""
                    delete from curtida_comentario
                    where comentario_id in (
                        select id from comentario
                        where postagem_id in (
                            select id from postagem where titulo like 'Postagem de teste %'
                        )
                    )
                    """);
            jdbcTemplate.update("""
                    delete from curtida_postagem
                    where postagem_id in (
                        select id from postagem where titulo like 'Postagem de teste %'
                    )
                    """);
            jdbcTemplate.update("""
                    delete from comentario
                    where postagem_id in (
                        select id from postagem where titulo like 'Postagem de teste %'
                    )
                    """);
            jdbcTemplate.update("delete from postagem where titulo like 'Postagem de teste %'");
        }
    }
}
