package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.dto.*;
import com.ifsc.prosigaa.enums.TipoPostagem;
import com.ifsc.prosigaa.model.*;
import com.ifsc.prosigaa.repository.AnexoPostagemRepository;
import com.ifsc.prosigaa.repository.PostagemRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import com.ifsc.prosigaa.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PostagemService {
    private static final Logger LOGGER = Logger.getLogger(PostagemService.class.getName());

    private final PostagemRepository postagemRepository;
    private final AnexoPostagemRepository anexoRepository;
    private final UsuarioService usuarioService;
    private final FaseService faseService;
    private final MateriaService materiaService;
    private final ArquivoPostagemStorage arquivoStorage;

    public PostagemService(PostagemRepository postagemRepository,
                           AnexoPostagemRepository anexoRepository,
                           UsuarioService usuarioService,
                           FaseService faseService,
                           MateriaService materiaService,
                           ArquivoPostagemStorage arquivoStorage) {
        this.postagemRepository = postagemRepository;
        this.anexoRepository = anexoRepository;
        this.usuarioService = usuarioService;
        this.faseService = faseService;
        this.materiaService = materiaService;
        this.arquivoStorage = arquivoStorage;
    }

    @Transactional
    public PostagemDTO cadastrar(CadastroPostagemDTO dto) {
        return cadastrar(dto, List.of());
    }

    @Transactional
    public PostagemDTO cadastrar(CadastroPostagemDTO dto, List<MultipartFile> arquivos) {
        validar(dto);
        arquivoStorage.validarArquivos(arquivos);

        Usuario usuario = usuarioService.buscar(dto.usuarioId());
        Fase fase = faseService.buscar(dto.faseId());
        Materia materia = materiaService.buscar(dto.materiaId());

        List<ArquivoPostagemStorage.ArquivoArmazenado> arquivosArmazenados = new ArrayList<>();
        try {
            if (arquivos != null) {
                for (MultipartFile arquivo : arquivos) {
                    arquivosArmazenados.add(arquivoStorage.salvar(arquivo));
                }
            }
        } catch (IOException e) {
            excluirArquivos(arquivosArmazenados, e);
            throw new UncheckedIOException(e);
        } catch (RuntimeException e) {
            excluirArquivos(arquivosArmazenados, e);
            throw e;
        }
        agendarLimpezaSeTransacaoFalhar(arquivosArmazenados);

        Postagem postagem = new Postagem(dto.titulo(), dto.conteudo(), dto.urlImagem(),
                dto.tipo(), usuario, fase, materia, dto.anonima());
        arquivosArmazenados.forEach(arquivo -> postagem.adicionarAnexo(new AnexoPostagem(
                arquivo.nomeOriginal(), arquivo.chaveArquivo(), arquivo.tipoConteudo(),
                arquivo.tamanhoBytes(), postagem
        )));

        return toDTO(postagemRepository.save(postagem));
    }

    @Transactional(readOnly = true)
    public List<PostagemDTO> listar(Long faseId, Long materiaId) {
        List<Postagem> postagens;

        if (faseId != null && materiaId != null) {
            postagens = postagemRepository.findByFaseIdAndMateriaIdOrderByCriadoEmDesc(faseId, materiaId);
        } else if (faseId != null) {
            postagens = postagemRepository.findByFaseIdOrderByCriadoEmDesc(faseId);
        } else if (materiaId != null) {
            postagens = postagemRepository.findByMateriaIdOrderByCriadoEmDesc(materiaId);
        } else {
            postagens = postagemRepository.findAllByOrderByCriadoEmDesc();
        }

        return postagens.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Postagem buscar(Long id) {
        return postagemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Postagem não encontrada."));
    }

    @Transactional(readOnly = true)
    public PostagemDTO buscarDTO(Long id) {
        return toDTO(buscar(id));
    }

    @Transactional(readOnly = true)
    public PostagemDTO toDTO(Postagem p) {
        FaseDTO fase = new FaseDTO(p.getFase().getId(), p.getFase().getNome(), p.getFase().getNumero(), List.of());
        MateriaDTO materia = new MateriaDTO(p.getMateria().getId(), p.getMateria().getNome(), p.getMateria().getCodigo());
        UsuarioResumoDTO usuario = new UsuarioResumoDTO(
                p.getUsuario().getId(),
                p.isAnonima() ? "Anônimo" : p.getUsuario().getNome(),
                p.getUsuario().getTipo()
        );

        List<ComentarioDTO> comentarios = p.getComentarios().stream()
                .map(c -> new ComentarioDTO(
                        c.getId(), c.getConteudo(), c.getCriadoEm(),
                        new UsuarioResumoDTO(c.getUsuario().getId(), c.getUsuario().getNome(), c.getUsuario().getTipo()),
                        c.getCurtidas().size()
                ))
                .toList();
        List<AnexoPostagemDTO> anexos = p.getAnexos().stream()
                .map(anexo -> new AnexoPostagemDTO(
                        anexo.getId(),
                        anexo.getNomeOriginal(),
                        anexo.getTipoConteudo(),
                        anexo.getTamanhoBytes(),
                        "/postagens/anexos/" + anexo.getId()
                ))
                .toList();

        return new PostagemDTO(
                p.getId(), p.getTitulo(), p.getConteudo(), p.getUrlImagem(),
                p.getTipo(), p.getCriadoEm(), usuario, fase, materia,
                comentarios, p.getCurtidas().size(), p.isAnonima(), anexos
        );
    }

    @Transactional(readOnly = true)
    public AnexoPostagem buscarAnexo(Long id) {
        return anexoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Arquivo não encontrado."));
    }

    private void validar(CadastroPostagemDTO dto) {
        if (dto.titulo() == null || dto.titulo().isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório.");
        }
        if (dto.conteudo() == null || dto.conteudo().isBlank()) {
            throw new IllegalArgumentException("Conteúdo é obrigatório.");
        }
        if (dto.tipo() == null) {
            throw new IllegalArgumentException("Tipo da postagem é obrigatório.");
        }
        if (dto.usuarioId() == null || dto.faseId() == null || dto.materiaId() == null) {
            throw new IllegalArgumentException("Usuário, fase e matéria são obrigatórios.");
        }
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("Informe o usuário que está excluindo a postagem.");
        }

        Postagem postagem = buscar(id);

        if (!postagem.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Você só pode excluir suas próprias postagens.");
        }

        List<String> chavesArquivos = postagem.getAnexos().stream()
                .map(AnexoPostagem::getChaveArquivo)
                .toList();
        postagemRepository.delete(postagem);
        agendarExclusaoAposConfirmacao(chavesArquivos);
    }

    public Resource carregarAnexo(AnexoPostagem anexo) throws IOException {
        return arquivoStorage.carregar(anexo.getChaveArquivo());
    }

    private void agendarLimpezaSeTransacaoFalhar(
            List<ArquivoPostagemStorage.ArquivoArmazenado> arquivos) {
        if (!TransactionSynchronizationManager.isSynchronizationActive() || arquivos.isEmpty()) return;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status != STATUS_COMMITTED) {
                    arquivos.forEach(arquivo -> excluirArquivoComLog(arquivo.chaveArquivo()));
                }
            }
        });
    }

    private void agendarExclusaoAposConfirmacao(List<String> chavesArquivos) {
        if (!TransactionSynchronizationManager.isSynchronizationActive() || chavesArquivos.isEmpty()) return;
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                chavesArquivos.forEach(PostagemService.this::excluirArquivoComLog);
            }
        });
    }

    private void excluirArquivos(List<ArquivoPostagemStorage.ArquivoArmazenado> arquivos, Exception causa) {
        for (var arquivo : arquivos) {
            try {
                arquivoStorage.excluir(arquivo.chaveArquivo());
            } catch (IOException erroLimpeza) {
                causa.addSuppressed(erroLimpeza);
                LOGGER.log(Level.SEVERE, "Não foi possível remover um anexo após falha no cadastro.", erroLimpeza);
            }
        }
    }

    private void excluirArquivoComLog(String chaveArquivo) {
        try {
            arquivoStorage.excluir(chaveArquivo);
        } catch (IOException erroLimpeza) {
            LOGGER.log(Level.SEVERE, "Não foi possível remover um arquivo de anexo.", erroLimpeza);
        }
    }

}
