# ProSIGAA — Backend

Backend REST do ProSIGAA, uma aplicação acadêmica para estudantes e professores compartilharem postagens relacionadas às fases e matérias, comentarem e curtirem conteúdos.

## Tecnologias

- Java 21
- Spring Boot 3.4.3
- Gradle Wrapper
- Spring Web (REST)
- Spring Data JPA / Hibernate
- H2 (banco relacional em arquivo)
- springdoc-openapi / Swagger UI
- Spring WebSocket
- JUnit 5 e Spring Boot Test

## Pré-requisitos

- JDK 21 instalado e configurado em `JAVA_HOME`.
- Acesso à internet na primeira compilação para baixar dependências Gradle, caso ainda não estejam em cache.

## Executar

Na raiz do projeto:

```bash
./gradlew bootRun
```

No Windows:

```bat
gradlew.bat bootRun
```

A API fica em `http://localhost:8080`. A documentação interativa fica em `http://localhost:8080/swagger-ui.html` e o documento OpenAPI JSON em `http://localhost:8080/v3/api-docs`.

O banco de desenvolvimento é criado no diretório `data/` e mantém os dados entre reinicializações. O console H2 está disponível em `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/prosigaa`, usuário `sa`, senha vazia). Não exponha o console H2 em produção.

## Testes

```bash
./gradlew test
```

Os testes usam banco H2 em memória, isolado do banco local. O relatório fica em `build/reports/tests/test/index.html`.

## Principais endpoints

| Método | Caminho | Função |
|---|---|---|
| GET | `/usuario` | Lista usuários |
| POST | `/usuario/cadastro` | Cadastra usuário institucional |
| POST | `/usuario/login` | Autentica usuário (protótipo) |
| GET | `/fase` | Lista fases e matérias associadas |
| POST | `/fase` | Cadastra fase |
| GET | `/materia` | Lista matérias; aceita `?faseId=` |
| POST | `/materia` | Cadastra matéria associada a uma fase |
| GET | `/postagem` | Lista postagens; aceita `?faseId=` e/ou `?materiaId=` |
| GET | `/postagem/{id}` | Consulta uma postagem |
| POST | `/postagem` | Cria postagem |
| POST | `/postagem/{id}/comentario` | Comenta em uma postagem |
| POST | `/curtida/postagem/{postagemId}?usuarioId={id}` | Curte uma postagem |
| POST | `/curtida/comentario/{comentarioId}?usuarioId={id}` | Curte um comentário |

Consulte o Swagger para os schemas e exemplos dos DTOs. Os enums aceitos estão em `src/main/java/com/ifsc/prosigaa/enums`.

## Exemplo de cadastro

```json
{
  "nome": "Maria da Silva",
  "email": "maria@ifsc.edu.br",
  "senha": "senha-de-exemplo",
  "tipo": "ALUNO"
}
```

Use `POST /usuario/cadastro`. Os tipos disponíveis devem corresponder ao enum `TipoUsuario`.

## Arquitetura

A aplicação segue organização em camadas:

- **controller**: endpoints HTTP e conversão de requisições/respostas;
- **dto**: contratos de entrada e saída da API;
- **service**: regras de negócio e validações;
- **repository**: persistência com Spring Data JPA;
- **model**: entidades persistidas;
- **config**: configuração do Spring, dados iniciais e OpenAPI.

## Limitações do protótipo

O login atual é uma autenticação demonstrativa, não um mecanismo de segurança de produção. Antes de publicar o sistema, implementar hash de senhas (por exemplo BCrypt), autenticação/autorização (por exemplo Spring Security + JWT), validação de entrada mais completa, política de CORS por ambiente, migrações de banco (Flyway/Liquibase), paginação e logs/auditoria. O banco H2 em arquivo atende à demonstração; produção deve usar banco gerenciado e credenciais via variáveis de ambiente.

## Documentação da iteração

Ver [`docs/`](docs/): casos de uso, especificação REST, arquitetura, modelo relacional/ER, testes, [notificações WebSocket](docs/WEBSOCKET_NOTIFICACOES.md) e declaração de uso de IA.
