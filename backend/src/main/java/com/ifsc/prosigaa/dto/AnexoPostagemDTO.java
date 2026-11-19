package com.ifsc.prosigaa.dto;

public record AnexoPostagemDTO(
        Long id,
        String nome,
        String tipoConteudo,
        long tamanhoBytes,
        String url
) {
}
