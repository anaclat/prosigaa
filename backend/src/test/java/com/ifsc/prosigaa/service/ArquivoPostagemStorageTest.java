package com.ifsc.prosigaa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArquivoPostagemStorageTest {
    @TempDir
    Path diretorio;

    private ArquivoPostagemStorage storage;

    @BeforeEach
    void configurar() throws IOException {
        storage = new ArquivoPostagemStorage(diretorio.toString());
        storage.prepararDiretorio();
    }

    @Test
    void deveArmazenarImagemComNomeGeradoEGuardarNomeOriginal() throws IOException {
        byte[] png = new byte[]{
                (byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A, 0x00
        };
        var arquivo = new MockMultipartFile("arquivos", "minha imagem.png", "image/png", png);

        storage.validarArquivos(List.of(arquivo));
        var armazenado = storage.salvar(arquivo);

        assertEquals("minha imagem.png", armazenado.nomeOriginal());
        assertEquals("image/png", armazenado.tipoConteudo());
        assertTrue(storage.carregar(armazenado.chaveArquivo()).exists());
        try (var arquivos = Files.list(diretorio)) {
            assertEquals(1, arquivos.count());
        }
    }

    @Test
    void deveRecusarArquivoComExtensaoPermitidaMasAssinaturaInvalida() {
        var arquivo = new MockMultipartFile("arquivos", "arquivo.png", "image/png", "não é png".getBytes());

        assertThrows(IllegalArgumentException.class, () -> storage.validarArquivos(List.of(arquivo)));
    }

    @Test
    void deveRecusarMaisDeCincoArquivos() {
        var arquivo = new MockMultipartFile("arquivos", "arquivo.png", "image/png", new byte[]{
                (byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A
        });

        assertThrows(IllegalArgumentException.class, () ->
                storage.validarArquivos(List.of(arquivo, arquivo, arquivo, arquivo, arquivo, arquivo)));
    }
}
