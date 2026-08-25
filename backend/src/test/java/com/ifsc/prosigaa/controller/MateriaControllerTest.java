package com.ifsc.prosigaa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsc.prosigaa.dto.CadastroMateriaDTO;
import com.ifsc.prosigaa.model.Materia;
import com.ifsc.prosigaa.service.MateriaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MateriaController.class)
class MateriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MateriaService materiaService;

    @Test
    void deveListarMateriasComParametroOpcional() throws Exception {
        when(materiaService.listar(1L)).thenReturn(List.of());

        mockMvc.perform(get("/materia").param("faseId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deveCadastrarMateriaComSucesso() throws Exception {
        CadastroMateriaDTO dto = new CadastroMateriaDTO("Matemática", "MAT101", 1L);

        Materia materiaMock = Mockito.mock(Materia.class);
        when(materiaMock.getId()).thenReturn(10L);
        when(materiaMock.getNome()).thenReturn("Matemática");
        when(materiaMock.getCodigo()).thenReturn("MAT101");

        when(materiaService.cadastrar("Matemática", "MAT101", 1L)).thenReturn(materiaMock);

        mockMvc.perform(post("/materia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nome").value("Matemática"));
    }
}