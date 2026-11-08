# Arquitetura e modelo de dados

## Arquitetura da aplicação

```mermaid
flowchart LR
    U[Usuário] --> FE[Frontend React]
    FE -->|HTTP/JSON| C[Controllers REST]
    FE <-->|SockJS/STOMP em /ws| WB[Broker WebSocket]
    C --> DTO[DTOs de entrada e saída]
    C --> S[Services]
    S --> R[Repositories Spring Data JPA]
    R --> JPA[JPA / Hibernate]
    JPA --> DB[(H2)]
    S --> NS[NotificacaoService]
    NS --> WB
    OE[OpenAPI / Swagger] -. documenta .-> C
    EH[ApiExceptionHandler] -. trata exceções REST .-> C
```

O frontend React consome a API REST e conecta-se ao endpoint WebSocket `/ws` para receber notificações em tópicos. Controllers definem o contrato HTTP e delegam regras aos services. DTOs definem os payloads expostos, repositories encapsulam o acesso JPA, e as entidades representam os dados persistidos. As notificações são enviadas em tempo real pelo broker simples do Spring; não há entidade de notificação persistida.

## Diagrama de classes das entidades

O diagrama abaixo reflete os atributos e associações JPA das entidades atuais. Não inclui DTOs nem serviços, pois representa o modelo persistido.

```mermaid
classDiagram
    class Usuario {
      Long id
      String nome
      String email
      String senhaHash
      TipoUsuario tipo
      LocalDateTime criadoEm
    }
    class Fase {
      Long id
      String nome
      int numero
    }
    class Materia {
      Long id
      String nome
      String codigo
    }
    class Postagem {
      Long id
      String titulo
      String conteudo
      String urlImagem
      TipoPostagem tipo
      LocalDateTime criadoEm
    }
    class Comentario {
      Long id
      String conteudo
      LocalDateTime criadoEm
    }
    class CurtidaPostagem {
      Long id
      LocalDateTime criadoEm
    }
    class CurtidaComentario {
      Long id
      LocalDateTime criadoEm
    }
    class TipoUsuario {
      <<enumeration>>
      ALUNO
      PROFESSOR
    }
    class TipoPostagem {
      <<enumeration>>
      DUVIDA
      MATERIAL_ESTUDO
    }

    Usuario --> TipoUsuario : tipo
    Postagem --> TipoPostagem : tipo
    Usuario "1" --> "0..*" Postagem : autor
    Usuario "1" --> "0..*" Comentario : autor
    Usuario "1" --> "0..*" CurtidaPostagem : registra
    Usuario "1" --> "0..*" CurtidaComentario : registra
    Fase "0..*" -- "0..*" Materia : fase_materia
    Fase "1" --> "0..*" Postagem : organiza
    Materia "1" --> "0..*" Postagem : classifica
    Postagem "1" --> "0..*" Comentario : recebe
    Postagem "1" --> "0..*" CurtidaPostagem : recebe
    Comentario "1" --> "0..*" CurtidaComentario : recebe
```

`TipoUsuario` é usado pela entidade `Usuario`; `TipoPostagem`, por `Postagem`. `TipoNotificacao` representa o tipo incluído nos DTOs das mensagens WebSocket e não é uma entidade persistida nem uma associação JPA. As coleções de comentários e curtidas são mapeadas com `cascade = ALL` a partir de `Postagem`; as curtidas de comentário também usam `cascade = ALL` a partir de `Comentario`. O relacionamento entre `Fase` e `Materia` é muitos-para-muitos, com tabela associativa `fase_materia`. A associação a usuário, fase, matéria e postagem é obrigatória nas entidades que a declaram como `@ManyToOne(optional = false)`.

## Diagrama entidade-relacionamento

```mermaid
erDiagram
    USUARIO ||--o{ POSTAGEM : publica
    USUARIO ||--o{ COMENTARIO : escreve
    USUARIO ||--o{ CURTIDA_POSTAGEM : registra
    USUARIO ||--o{ CURTIDA_COMENTARIO : registra
    FASE ||--o{ FASE_MATERIA : associa
    MATERIA ||--o{ FASE_MATERIA : associa
    FASE ||--o{ POSTAGEM : organiza
    MATERIA ||--o{ POSTAGEM : classifica
    POSTAGEM ||--o{ COMENTARIO : recebe
    POSTAGEM ||--o{ CURTIDA_POSTAGEM : recebe
    COMENTARIO ||--o{ CURTIDA_COMENTARIO : recebe

    USUARIO {
      bigint id PK
      varchar nome
      varchar email
      varchar senha_hash
      varchar tipo
      timestamp criado_em
    }
    FASE {
      bigint id PK
      varchar nome
      int numero
    }
    MATERIA {
      bigint id PK
      varchar nome
      varchar codigo
    }
    FASE_MATERIA {
      bigint fase_id FK
      bigint materia_id FK
    }
    POSTAGEM {
      bigint id PK
      varchar titulo
      text conteudo
      varchar url_imagem
      varchar tipo
      timestamp criado_em
      bigint usuario_id FK
      bigint fase_id FK
      bigint materia_id FK
    }
    COMENTARIO {
      bigint id PK
      text conteudo
      timestamp criado_em
      bigint usuario_id FK
      bigint postagem_id FK
    }
    CURTIDA_POSTAGEM {
      bigint id PK
      timestamp criado_em
      bigint usuario_id FK
      bigint postagem_id FK
    }
    CURTIDA_COMENTARIO {
      bigint id PK
      timestamp criado_em
      bigint usuario_id FK
      bigint comentario_id FK
    }
```

O esquema é gerenciado pelo Hibernate com `ddl-auto=update`; os nomes físicos podem variar conforme a estratégia de nomenclatura configurada pelo Spring Boot. Não há anotação `unique` declarada para e-mail ou para pares usuário/conteúdo das curtidas. O serviço evita duplicar curtidas em chamadas sequenciais, mas uma restrição de unicidade no banco ainda seria necessária para proteger contra concorrência.

## Sequência — publicar postagem

```mermaid
sequenceDiagram
    actor U as Usuário/Cliente
    participant C as PostagemController
    participant S as PostagemService
    participant US as UsuarioService
    participant FS as FaseService
    participant MS as MateriaService
    participant R as PostagemRepository
    U->>C: POST /postagens (CadastroPostagemDTO)
    C->>S: cadastrar(dto)
    S->>S: validar campos obrigatórios
    S->>US: buscar(usuarioId)
    US-->>S: Usuario
    S->>FS: buscar(faseId)
    FS-->>S: Fase
    S->>MS: buscar(materiaId)
    MS-->>S: Materia
    S->>R: save(Postagem)
    R-->>S: Postagem persistida
    S-->>C: PostagemDTO
    C-->>U: 201 Created
```

## Sequência — comentar postagem

```mermaid
sequenceDiagram
    actor U as Usuário/Cliente
    participant C as PostagemController
    participant CS as ComentarioService
    participant US as UsuarioService
    participant PS as PostagemService
    participant CR as ComentarioRepository
    participant NS as NotificacaoService
    participant WB as Broker WebSocket
    actor A as Autor da postagem
    U->>C: POST /postagens/{id}/comentarios
    C->>CS: cadastrar(id, dto)
    CS->>CS: validar conteúdo
    CS->>US: buscar(usuarioId)
    US-->>CS: Usuario
    CS->>PS: buscar(postagemId)
    PS-->>CS: Postagem
    CS->>CR: save(Comentario)
    CR-->>CS: Comentario persistido
    opt ator diferente do autor
        CS->>NS: notificarNovoComentario(...)
        NS->>WB: enviar para tópico do autor
        WB-->>A: Notificação
    end
    CS-->>C: Comentario
    C-->>U: 201 Created + postagem atualizada
```

## Sequência — curtir postagem

```mermaid
sequenceDiagram
    actor U as Usuário/Cliente
    participant C as CurtidaController
    participant S as CurtidaService
    participant UR as CurtidaPostagemRepository
    participant US as UsuarioService
    participant PS as PostagemService
    participant NS as NotificacaoService
    participant WB as Broker WebSocket
    actor A as Autor da postagem
    U->>C: POST /curtidas/postagens/{id} (CurtidaDTO)
    C->>S: curtirPostagem(id, usuarioId)
    S->>UR: existsByUsuarioIdAndPostagemId(...)
    alt curtida já existe
        S-->>C: sem nova gravação
    else nova curtida
        S->>US: buscar(usuarioId)
        US-->>S: Usuario
        S->>PS: buscar(postagemId)
        PS-->>S: Postagem
        S->>UR: save(CurtidaPostagem)
        opt ator diferente do autor
            S->>NS: notificarCurtidaPostagem(...)
            NS->>WB: enviar para tópico do autor
            WB-->>A: Notificação
        end
    end
    C-->>U: 201 Created
```

## Carregamento de associações

As associações percorridas ao montar DTOs possuem `@BatchSize(size = 50)` e a aplicação configura `hibernate.default_batch_fetch_size=50`. Essa configuração permite que o Hibernate carregue associações lazy em lotes. A redução efetiva de consultas deve ser verificada por teste de integração com estatísticas SQL; a anotação, isoladamente, não prova que todo caminho de leitura esteja livre de N+1.
