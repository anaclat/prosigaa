# Arquitetura e modelo de dados

## Arquitetura lógica

```mermaid
flowchart TD
    Cliente[Cliente Web / Aplicação] -->|HTTP JSON| C[Controllers REST]
    Cliente <-->|WebSocket, quando usado| WS[Configuração WebSocket]
    C --> D[DTOs]
    C --> S[Services]
    S --> R[Repositories Spring Data JPA]
    R --> ORM[Hibernate / JPA]
    ORM --> DB[(Banco relacional H2)]
    CFG[Configuração / OpenAPI / Dados iniciais] --> C
```

Controllers cuidam do protocolo HTTP; services concentram regras de negócio; repositories encapsulam operações de persistência; entities representam tabelas e relacionamentos. DTOs evitam expor diretamente entidades na API.

## Diagrama de classes (modelo estrutural simplificado)

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
    Usuario "1" --> "0..*" Postagem : publica
    Usuario "1" --> "0..*" Comentario : escreve
    Fase "1" --> "0..*" Postagem : organiza
    Materia "1" --> "0..*" Postagem : classifica
    Fase "0..*" -- "0..*" Materia : fase_materia
    Postagem "1" --> "0..*" Comentario : recebe
    Postagem "1" --> "0..*" CurtidaPostagem : possui
    Comentario "1" --> "0..*" CurtidaComentario : possui
    Usuario "1" --> "0..*" CurtidaPostagem : registra
    Usuario "1" --> "0..*" CurtidaComentario : registra
```

## Diagrama entidade-relacionamento (ER)

```mermaid
erDiagram
    USUARIO ||--o{ POSTAGEM : publica
    USUARIO ||--o{ COMENTARIO : escreve
    FASE ||--o{ POSTAGEM : organiza
    MATERIA ||--o{ POSTAGEM : classifica
    FASE }o--o{ MATERIA : fase_materia
    POSTAGEM ||--o{ COMENTARIO : recebe
    USUARIO ||--o{ CURTIDA_POSTAGEM : registra
    POSTAGEM ||--o{ CURTIDA_POSTAGEM : recebe
    USUARIO ||--o{ CURTIDA_COMENTARIO : registra
    COMENTARIO ||--o{ CURTIDA_COMENTARIO : recebe
    USUARIO {
      bigint id PK
      varchar nome
      varchar email UK
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

**Observação:** os nomes físicos exatos das tabelas/colunas podem ser normalizados pelo Hibernate conforme a estratégia de nomenclatura. `fase_materia` é a tabela associativa do relacionamento muitos-para-muitos. As entidades de curtida não têm ainda uma restrição única para impedir duplicidade do mesmo usuário no mesmo conteúdo; isso é uma melhoria recomendada.

## Fluxo comportamental — publicação

```mermaid
sequenceDiagram
    actor U as Usuário
    participant C as PostagemController
    participant S as PostagemService
    participant UR as UsuarioService
    participant FR as FaseService
    participant MR as MateriaService
    participant R as PostagemRepository
    U->>C: POST /postagem (DTO)
    C->>S: cadastrar(dto)
    S->>S: validar campos obrigatórios
    S->>UR: buscar(usuarioId)
    S->>FR: buscar(faseId)
    S->>MR: buscar(materiaId)
    S->>R: save(Postagem)
    R-->>S: entidade persistida
    S-->>C: PostagemDTO
    C-->>U: resposta JSON
```
