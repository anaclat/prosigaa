package com.ifsc.prosigaa.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsc.prosigaa.dto.CadastroPostagemDTO;
import com.ifsc.prosigaa.enums.TipoPostagem;
import com.ifsc.prosigaa.repository.FaseRepository;
import com.ifsc.prosigaa.repository.MateriaRepository;
import com.ifsc.prosigaa.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AnexoPostagemIntegrationTest {
    @TempDir
    static Path uploadDirectory;

    @DynamicPropertySource
    static void configurarDiretorioDeUpload(DynamicPropertyRegistry registry) {
        registry.add("app.upload.dir", () -> uploadDirectory.toString());
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:anexo-postagem-integration;DB_CLOSE_DELAY=-1");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FaseRepository faseRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    @Test
    void devePersistirEDisponibilizarArquivoAnexado() throws Exception {
        var usuario = usuarioRepository.findByEmail("aluno@ifsc.edu.br").orElseThrow();
        var fase = faseRepository.findByNumero(3).orElseThrow();
        var materia = materiaRepository.findByNome("Estrutura de Dados").orElseThrow();
        var dadosPostagem = new CadastroPostagemDTO(
                "Material com arquivo", "Conteúdo de teste", null, TipoPostagem.MATERIAL_ESTUDO,
                usuario.getId(), fase.getId(), materia.getId(), false
        );
        byte[] conteudoPdf = "%PDF-1.7\nconteudo de teste".getBytes();
        var dados = new MockMultipartFile(
                "dados", "dados.json", "application/json", objectMapper.writeValueAsBytes(dadosPostagem)
        );
        var arquivo = new MockMultipartFile(
                "arquivos", "resumo.pdf", "application/pdf", conteudoPdf
        );

        var resposta = mockMvc.perform(multipart("/postagens").file(dados).file(arquivo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.anexos[0].nome").value("resumo.pdf"))
                .andExpect(jsonPath("$.anexos[0].tipoConteudo").value("application/pdf"))
                .andReturn();
        long anexoId = objectMapper.readTree(resposta.getResponse().getContentAsByteArray())
                .path("anexos").get(0).path("id").asLong();

        mockMvc.perform(get("/postagens/anexos/{id}", anexoId))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString("attachment")))
                .andExpect(content().bytes(conteudoPdf));
    }
}
