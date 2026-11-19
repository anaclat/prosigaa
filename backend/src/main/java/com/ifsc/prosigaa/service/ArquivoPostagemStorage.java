package com.ifsc.prosigaa.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class ArquivoPostagemStorage {
    public static final int MAX_ARQUIVOS = 5;
    public static final long MAX_TAMANHO_ARQUIVO = 10L * 1024 * 1024;

    private final Path diretorio;

    public ArquivoPostagemStorage(@Value("${app.upload.dir:./data/uploads}") String diretorio) {
        this.diretorio = Path.of(diretorio).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void prepararDiretorio() throws IOException {
        Files.createDirectories(diretorio);
    }

    public void validarArquivos(List<MultipartFile> arquivos) {
        if (arquivos == null || arquivos.isEmpty()) return;
        if (arquivos.size() > MAX_ARQUIVOS) {
            throw new IllegalArgumentException("Adicione no máximo 5 arquivos por publicação.");
        }

        long tamanhoTotal = 0;
        for (MultipartFile arquivo : arquivos) {
            if (arquivo == null || arquivo.isEmpty()) {
                throw new IllegalArgumentException("Não é possível adicionar um arquivo vazio.");
            }
            if (arquivo.getSize() > MAX_TAMANHO_ARQUIVO) {
                throw new IllegalArgumentException("Cada arquivo deve ter no máximo 10 MB.");
            }
            tamanhoTotal += arquivo.getSize();
            validarExtensaoEConteudo(arquivo);
        }
        if (tamanhoTotal > MAX_ARQUIVOS * MAX_TAMANHO_ARQUIVO) {
            throw new IllegalArgumentException("O tamanho total dos anexos excede o limite permitido.");
        }
    }

    public ArquivoArmazenado salvar(MultipartFile arquivo) throws IOException {
        String nomeOriginal = nomeSeguro(arquivo.getOriginalFilename());
        String extensao = extensao(nomeOriginal);
        String tipoConteudo = tipoConteudo(extensao);
        String chaveArquivo = UUID.randomUUID() + extensao;
        Path destino = resolver(chaveArquivo);

        try (InputStream entrada = arquivo.getInputStream()) {
            Files.copy(entrada, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException erro) {
            try {
                Files.deleteIfExists(destino);
            } catch (IOException erroLimpeza) {
                erro.addSuppressed(erroLimpeza);
            }
            throw erro;
        }

        return new ArquivoArmazenado(nomeOriginal, chaveArquivo, tipoConteudo, arquivo.getSize());
    }

    public Resource carregar(String chaveArquivo) throws IOException {
        Path arquivo = resolver(chaveArquivo);
        if (!Files.isRegularFile(arquivo)) {
            throw new IOException("Arquivo de anexo não encontrado.");
        }
        return new FileSystemResource(arquivo);
    }

    public void excluir(String chaveArquivo) throws IOException {
        Files.deleteIfExists(resolver(chaveArquivo));
    }

    private void validarExtensaoEConteudo(MultipartFile arquivo) {
        String nome = nomeSeguro(arquivo.getOriginalFilename());
        String extensao = extensao(nome);
        byte[] assinatura;
        try (InputStream entrada = arquivo.getInputStream()) {
            assinatura = entrada.readNBytes(8);
        } catch (IOException e) {
            throw new IllegalArgumentException("Não foi possível ler um dos arquivos enviados.", e);
        }

        boolean valido = switch (extensao) {
            case ".pdf" -> comecaCom(assinatura, new byte[]{'%', 'P', 'D', 'F', '-'});
            case ".png" -> comecaCom(assinatura, new byte[]{
                    (byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A
            });
            case ".jpg", ".jpeg" -> comecaCom(assinatura, new byte[]{
                    (byte) 0xFF, (byte) 0xD8, (byte) 0xFF
            });
            default -> false;
        };
        if (!valido) {
            throw new IllegalArgumentException("Envie um arquivo PDF, PNG ou JPG válido.");
        }
    }

    private boolean comecaCom(byte[] valor, byte[] prefixo) {
        if (valor.length < prefixo.length) return false;
        for (int i = 0; i < prefixo.length; i++) {
            if (valor[i] != prefixo[i]) return false;
        }
        return true;
    }

    private String tipoConteudo(String extensao) {
        return switch (extensao) {
            case ".pdf" -> "application/pdf";
            case ".png" -> "image/png";
            case ".jpg", ".jpeg" -> "image/jpeg";
            default -> throw new IllegalArgumentException("Formato de arquivo não permitido.");
        };
    }

    private String nomeSeguro(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do arquivo é inválido.");
        }
        String nomeBase = nome.replace('\\', '/');
        nomeBase = nomeBase.substring(nomeBase.lastIndexOf('/') + 1)
                .replaceAll("[\\p{Cntrl}]", "_")
                .trim();
        if (nomeBase.isBlank()) {
            throw new IllegalArgumentException("O nome do arquivo é inválido.");
        }
        return nomeBase.length() > 200 ? nomeBase.substring(nomeBase.length() - 200) : nomeBase;
    }

    private String extensao(String nome) {
        int ponto = nome.lastIndexOf('.');
        if (ponto < 0) return "";
        return nome.substring(ponto).toLowerCase(Locale.ROOT);
    }

    private Path resolver(String chaveArquivo) throws IOException {
        Path arquivo = diretorio.resolve(chaveArquivo).normalize();
        if (!arquivo.getParent().equals(diretorio)) {
            throw new IOException("Caminho de arquivo inválido.");
        }
        return arquivo;
    }

    public record ArquivoArmazenado(String nomeOriginal, String chaveArquivo,
                                    String tipoConteudo, long tamanhoBytes) {
    }
}
