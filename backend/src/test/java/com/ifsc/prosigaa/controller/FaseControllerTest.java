package com.ifsc.prosigaa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifsc.prosigaa.dto.CadastroFaseDTO;
import com.ifsc.prosigaa.dto.FasesDTO;
import com.ifsc.prosigaa.model.Fase;
import com.ifsc.prosigaa.service.FaseService;
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

@WebMvcTest(FaseController.class)
class FaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FaseService faseService;

    @Test
    void deveListarFasesComSucesso() throws Exception {
        FasesDTO dtoMock = new FasesDTO(List.of());
        when(faseService.listar()).thenReturn(dtoMock);

        mockMvc.perform(get("/fase"))
                .andExpect(status().isOk());
    }

    @Test
    void deveCadastrarFaseComSucesso() throws Exception {
        CadastroFaseDTO dto = new CadastroFaseDTO("Fase 1", 1);

        Fase faseMock = Mockito.mock(Fase.class);
        when(faseMock.getId()).thenReturn(1L);
        when(faseMock.getNome()).thenReturn("Fase 1");
        when(faseMock.getNumero()).thenReturn(1);

        when(faseService.cadastrar("Fase 1", 1)).thenReturn(faseMock);

        mockMvc.perform(post("/fase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Fase 1"));
    }
}