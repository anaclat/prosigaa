package com.ifsc.prosigaa.config;

import com.ifsc.prosigaa.enums.TipoPostagem;
import com.ifsc.prosigaa.enums.TipoUsuario;
import com.ifsc.prosigaa.model.Comentario;
import com.ifsc.prosigaa.model.Fase;
import com.ifsc.prosigaa.model.Materia;
import com.ifsc.prosigaa.model.Postagem;
import com.ifsc.prosigaa.model.Usuario;
import com.ifsc.prosigaa.repository.ComentarioRepository;
import com.ifsc.prosigaa.repository.FaseRepository;
import com.ifsc.prosigaa.repository.MateriaRepository;
import com.ifsc.prosigaa.repository.PostagemRepository;
import com.ifsc.prosigaa.repository.UsuarioRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class DadosIniciais implements ApplicationRunner {
    private static final List<PeriodoInicial> CURRICULO = List.of(
            new PeriodoInicial(1, "1º período", List.of(
                    "Pensamento Computacional e Algoritmos",
                    "Fundamentos de Arquitetura e Organização de Computadores",
                    "Fundamentos da Matemática",
                    "Introdução à Computação",
                    "Inglês Instrumental I",
                    "Lógica Matemática"
            )),
            new PeriodoInicial(2, "2º período", List.of(
                    "Atividades de Extensão I",
                    "Comunicação e Expressão",
                    "Fundamentos de Probabilidade e Estatística",
                    "Programação Frontend I",
                    "Programação Orientada a Objetos",
                    "Redes de Computadores"
            )),
            new PeriodoInicial(3, "3º período", List.of(
                    "Banco de Dados",
                    "Estrutura de Dados",
                    "Engenharia de Software I",
                    "Programação Frontend II",
                    "Segurança da Informação"
            )),
            new PeriodoInicial(4, "4º período", List.of(
                    "Atividades de Extensão II",
                    "Engenharia de Software II",
                    "Interface Humano-Computador",
                    "Programação Backend",
                    "Sistemas Operacionais"
            )),
            new PeriodoInicial(5, "5º período", List.of(
                    "Inglês Instrumental II",
                    "Libras",
                    "Tópicos Especiais",
                    "Atividades de Extensão III",
                    "Introdução à Ciência de Dados e Aprendizado de Máquina",
                    "Infraestrutura e Serviços em Nuvem",
                    "Programação para Aplicativos Móveis"
            ))
    );

    private static final List<PostagemInicial> POSTAGENS = List.of(
            new PostagemInicial(
                    "Quando usar pilha ou fila?",
                    "Como identificar qual estrutura de dados faz mais sentido em cada exercício?",
                    TipoPostagem.DUVIDA,
                    1,
                    "Pensamento Computacional e Algoritmos",
                    "Pergunta de João P.",
                    List.of(
                            new ComentarioInicial("Na pilha, o último elemento que entra é o primeiro a sair. Já na fila, o primeiro que entra é o primeiro a sair.", "Camila S.", false),
                            new ComentarioInicial("Pense em pilha como uma pilha de pratos: você sempre remove o de cima. Em fila, você atende quem chegou primeiro.", "Lucas R.", false),
                            new ComentarioInicial("A decisão depende da regra de processamento. Para LIFO, use pilha; para FIFO, fila.", "Prof. Manoel", true)
                    )
            ),
            new PostagemInicial(
                    "Como resolver expressões na Lógica Matemática?",
                    "Estou revisando para a prova e tenho dúvidas sobre equivalências lógicas. Alguém consegue explicar com exemplo?",
                    TipoPostagem.DUVIDA,
                    1,
                    "Lógica Matemática",
                    "Pergunta de Luiza M.",
                    List.of(
                            new ComentarioInicial("Comecei fatorando os números comuns e depois simplifiquei a expressão. O mais importante é separar cada termo.", "João P.", false),
                            new ComentarioInicial("A fatoração é uma forma de reescrever a expressão como produto de termos mais simples. A prática ajuda muito.", "Prof. Renan", true)
                    )
            ),
            new PostagemInicial(
                    "Como organizar componentes no Frontend I?",
                    "Qual é uma boa forma de separar os componentes e organizar as pastas de um projeto React?",
                    TipoPostagem.DUVIDA,
                    2,
                    "Programação Frontend I",
                    "Pergunta de Rafael T.",
                    List.of(
                            new ComentarioInicial("Elas atuam em corpos diferentes. Portanto, não se anulam em um único corpo.", "Marta L.", false),
                            new ComentarioInicial("Exatamente: a terceira lei de Newton trata de pares de ação e reação em corpos distintos.", "Prof. Cassia", true)
                    )
            ),
            new PostagemInicial(
                    "Resumo visual: estruturas de dados",
                    "Um mapa rápido para revisar listas, filas, pilhas e árvores antes da próxima aula.",
                    TipoPostagem.MATERIAL_ESTUDO,
                    3,
                    "Estrutura de Dados",
                    "João P.",
                    List.of(
                            new ComentarioInicial("Esse resumo me ajudou bastante na revisão antes da atividade.", "Ana B.", false),
                            new ComentarioInicial("Ótimo material. O gráfico visual ajuda bastante a entender o comportamento de cada estrutura.", "Prof. Thiago", true)
                    )
            )
    );

    private final FaseRepository fases;
    private final MateriaRepository materias;
    private final UsuarioRepository usuarios;
    private final PostagemRepository postagens;
    private final ComentarioRepository comentarios;
    private final PasswordEncoder passwordEncoder;

    public DadosIniciais(
            FaseRepository fases,
            MateriaRepository materias,
            UsuarioRepository usuarios,
            PostagemRepository postagens,
            ComentarioRepository comentarios,
            PasswordEncoder passwordEncoder
    ) {
        this.fases = fases;
        this.materias = materias;
        this.usuarios = usuarios;
        this.postagens = postagens;
        this.comentarios = comentarios;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Map<Integer, Fase> fasesCurriculares = prepararCurriculo();
        garantirUsuarioPadrao("Professor Exemplo", "professor@ifsc.edu.br", TipoUsuario.PROFESSOR);
        garantirUsuarioPadrao("Aluno Exemplo", "aluno@ifsc.edu.br", TipoUsuario.ALUNO);

        if (postagens.count() == 0) {
            criarPostagensIniciais(fasesCurriculares);
        }
    }

    private Map<Integer, Fase> prepararCurriculo() {
        Map<Integer, Fase> fasesCurriculares = new HashMap<>();
        for (PeriodoInicial periodo : CURRICULO) {
            Fase fase = fases.findByNumero(periodo.numero())
                    .orElseGet(() -> fases.save(new Fase(periodo.nome(), periodo.numero())));
            if (!fase.getNome().equals(periodo.nome())) {
                fase.renomear(periodo.nome());
            }
            removerAssociacoesLegadas(fase);

            for (String nomeMateria : periodo.materias()) {
                Materia materia = materias.findByNome(nomeMateria)
                        .orElseGet(() -> materias.save(new Materia(nomeMateria, "")));
                if (!fase.getMaterias().contains(materia)) {
                    fase.getMaterias().add(materia);
                    materia.getFases().add(fase);
                }
            }

            fases.save(fase);
            fasesCurriculares.put(periodo.numero(), fase);
        }
        return fasesCurriculares;
    }

    private void removerAssociacoesLegadas(Fase fase) {
        List<String> nomesLegados = switch (fase.getNumero()) {
            case 1 -> List.of("Programação I", "Cálculo I");
            case 2 -> List.of("Estrutura de Dados");
            default -> List.of();
        };

        for (Materia materia : List.copyOf(fase.getMaterias())) {
            if (nomesLegados.contains(materia.getNome())
                    && !postagens.existsByFaseIdAndMateriaId(fase.getId(), materia.getId())) {
                fase.getMaterias().remove(materia);
                materia.getFases().remove(fase);
            }
        }
    }

    private void criarPostagensIniciais(Map<Integer, Fase> fasesCurriculares) {
        for (PostagemInicial inicial : POSTAGENS) {
            Fase fase = fasesCurriculares.get(inicial.numeroFase());
            Materia materia = fase.getMaterias().stream()
                    .filter(item -> item.getNome().equals(inicial.materia()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "Matéria inicial não encontrada: " + inicial.materia()
                    ));
            Usuario autor = usuarioInicial(inicial.autor(), TipoUsuario.ALUNO);
            Postagem postagem = postagens.save(new Postagem(
                    inicial.titulo(),
                    inicial.conteudo(),
                    null,
                    inicial.tipo(),
                    autor,
                    fase,
                    materia
            ));

            for (ComentarioInicial comentarioInicial : inicial.comentarios()) {
                TipoUsuario tipo = comentarioInicial.professor() ? TipoUsuario.PROFESSOR : TipoUsuario.ALUNO;
                Comentario comentario = comentarios.save(new Comentario(
                        comentarioInicial.conteudo(),
                        usuarioInicial(comentarioInicial.autor(), tipo),
                        postagem
                ));
                postagem.adicionarComentario(comentario);
            }
        }
    }

    private Usuario garantirUsuarioPadrao(String nome, String email, TipoUsuario tipo) {
        return usuarios.findByEmail(email)
                .orElseGet(() -> usuarios.save(new Usuario(nome, email, passwordEncoder.encode("ProSigaa@123"), tipo)));
    }

    private Usuario usuarioInicial(String nome, TipoUsuario tipo) {
        String slug = Normalizer.normalize(nome, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        String email = "seed." + slug + "@prosigaa.local";
        return usuarios.findByEmail(email)
                .orElseGet(() -> usuarios.save(new Usuario(nome, email, passwordEncoder.encode(UUID.randomUUID().toString()), tipo)));
    }

    private record PeriodoInicial(int numero, String nome, List<String> materias) {
    }

    private record PostagemInicial(
            String titulo,
            String conteudo,
            TipoPostagem tipo,
            int numeroFase,
            String materia,
            String autor,
            List<ComentarioInicial> comentarios
    ) {
    }

    private record ComentarioInicial(String conteudo, String autor, boolean professor) {
    }
}
