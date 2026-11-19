package com.ifsc.prosigaa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsc.prosigaa.dto.CadastroComentarioDTO;
import com.ifsc.prosigaa.dto.CadastroPostagemDTO;
import com.ifsc.prosigaa.dto.PostagemDTO;
import com.ifsc.prosigaa.enums.TipoPostagem;
import com.ifsc.prosigaa.exception.ResourceNotFoundException;
import com.ifsc.prosigaa.model.Comentario;
import com.ifsc.prosigaa.model.Postagem;
import com.ifsc.prosigaa.service.ComentarioService;
import com.ifsc.prosigaa.service.PostagemService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(PostagemController.class)
class PostagemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostagemService postagemService;

    @MockitoBean
    private ComentarioService comentarioService;

    @Test
    void deveListarPostagens() throws Exception {
        when(postagemService.listar(null, null)).thenReturn(List.of());

        mockMvc.perform(get("/postagens"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveBuscarPostagemPorId() throws Exception {
        PostagemDTO dtoMock = Mockito.mock(PostagemDTO.class);

        when(postagemService.buscarDTO(1L)).thenReturn(dtoMock);

        mockMvc.perform(get("/postagens/1"))
        .andExpect(status().isOk());
    }

    @Test
    void deveCadastrarPostagem() throws Exception {
        CadastroPostagemDTO dto = new CadastroPostagemDTO("Título", "Conteúdo","URL" , TipoPostagem.DUVIDA, 1L, 1L, 1L);
        PostagemDTO dtoRetornoMock = Mockito.mock(PostagemDTO.class);

        when(postagemService.cadastrar(any(CadastroPostagemDTO.class))).thenReturn(dtoRetornoMock);

        mockMvc.perform(post("/postagens")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated());
    }

    @Test
    void deveReceberPreferenciaDePostagemAnonima() throws Exception {
        CadastroPostagemDTO dto = new CadastroPostagemDTO(
                "Título", "Conteúdo", null, TipoPostagem.DUVIDA, 1L, 1L, 1L, true
        );
        PostagemDTO dtoRetornoMock = Mockito.mock(PostagemDTO.class);
        when(postagemService.cadastrar(any(CadastroPostagemDTO.class))).thenReturn(dtoRetornoMock);

        mockMvc.perform(post("/postagens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        var captor = org.mockito.ArgumentCaptor.forClass(CadastroPostagemDTO.class);
        verify(postagemService).cadastrar(captor.capture());
        assertTrue(captor.getValue().anonima());
    }

    @Test
    void deveCadastrarPostagemComAnexoMultipart() throws Exception {
        PostagemDTO dtoRetornoMock = Mockito.mock(PostagemDTO.class);
        when(postagemService.cadastrar(any(CadastroPostagemDTO.class), any())).thenReturn(dtoRetornoMock);
        MockMultipartFile dados = new MockMultipartFile(
                "dados", "dados.json", MediaType.APPLICATION_JSON_VALUE,
                """
                        {"titulo":"Material","conteudo":"Resumo","tipo":"MATERIAL_ESTUDO",
                        "usuarioId":1,"faseId":1,"materiaId":1}
                        """.getBytes()
        );
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivos", "resumo.pdf", MediaType.APPLICATION_PDF_VALUE,
                "%PDF-1.7".getBytes()
        );

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .multipart("/postagens").file(dados).file(arquivo))
                .andExpect(status().isCreated());

        verify(postagemService).cadastrar(any(CadastroPostagemDTO.class), any());
    }

    @Test
    void deveAdicionarComentarioEmPostagem() throws Exception {
        CadastroComentarioDTO dto = new CadastroComentarioDTO("Comentário Teste", 1L);
        Comentario comentarioMock = Mockito.mock(Comentario.class);
        Postagem postagemMock = Mockito.mock(Postagem.class);
        PostagemDTO dtoRetornoMock = Mockito.mock(PostagemDTO.class);

        when(comentarioService.cadastrar(eq(1L), any(CadastroComentarioDTO.class))).thenReturn(comentarioMock);
        when(postagemService.buscarDTO(1L)).thenReturn(dtoRetornoMock);

        mockMvc.perform(post("/postagens/1/comentarios")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated());
    }

    @Test
    void deveRetornarNotFoundQuandoPostagemNaoExiste() throws Exception {
        when(postagemService.buscarDTO(999L))
                .thenThrow(new ResourceNotFoundException("Postagem não encontrada."));

        mockMvc.perform(get("/postagens/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value("Postagem não encontrada."));
    }

    @Test
    void deveRetornarBadRequestQuandoCorpoDaPostagemForInvalido() throws Exception {
        mockMvc.perform(post("/postagens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.mensagem").value("Corpo da requisição inválido ou incompleto."));
    }

    @Test
    void deveExcluirPostagemSemConteudoNaResposta() throws Exception {
        mockMvc.perform(delete("/postagens/1?usuarioId=1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void deveExcluirComentarioSemConteudoNaResposta() throws Exception {
        mockMvc.perform(delete("/postagens/comentarios/2?usuarioId=1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }
}
