# ProSIGAA — Backend

Backend REST do ProSIGAA, uma aplicação acadêmica para estudantes e professores compartilharem postagens relacionadas às fases e matérias, comentarem e curtirem conteúdos.

## Tecnologias

- Java 21
- Spring Boot 3.4.3
- Gradle Wrapper
- Spring Web (REST)
- Spring Data JPA / Hibernate
- Spring Security Crypto (BCrypt para hashes de senha)
- Spring Security Crypto (BCrypt para hashes de senha)
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

## Executar com Docker (container único)

O deploy recomendado está no `Dockerfile` da raiz do repositório e sobe frontend + backend em um único container, ideal para Coolify.

Na raiz do projeto:

```bash
docker build -t prosigaa .
docker run --rm -p 3000:80 -v $(pwd)/backend/data:/app/data prosigaa
```

No modo container único, a API fica disponível via proxy em `/api`:

- `http://localhost:3000/api/swagger-ui.html`

A API fica em `http://localhost:8080`. A documentação interativa fica em `http://localhost:8080/swagger-ui.html` e o documento OpenAPI JSON em `http://localhost:8080/v3/api-docs`.

O banco de desenvolvimento é criado no diretório `data/` e mantém os dados entre reinicializações. O console H2 está disponível em `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/prosigaa`, usuário `sa`, senha vazia). Não exponha o console H2 em produção.

### CORS por ambiente

As origens aceitas por CORS e WebSocket podem ser ajustadas via propriedade:

- `app.cors.allowed-origin-patterns`

Exemplo:

```bash
APP_CORS_ALLOWED_ORIGIN_PATTERNS="http://localhost:*,https://*.vercel.app,https://meu-frontend.com"
```

Valor padrão inclui localhost e domínios `*.vercel.app`; em produção ajuste para os domínios que você realmente usa.

Na primeira inicialização, o backend cadastra as cinco fases do currículo, suas matérias, os usuários de demonstração e exemplos de postagens e comentários. Esse conteúdo é persistido no H2; as postagens de exemplo só são inseridas quando ainda não há nenhuma postagem.

## Testes

```bash
./gradlew test
```

Os testes usam banco H2 em memória, isolado do banco local. O relatório fica em `build/reports/tests/test/index.html`.

## Principais endpoints

As rotas canônicas usam recursos no plural. A tabela completa, payloads e respostas estão em [`docs/API_REST.md`](docs/API_REST.md).

| Método | Caminho | Função |
|---|---|---|
| POST / GET | `/usuarios` | Cadastrar / listar dados resumidos de usuários |
| POST | `/usuarios/login` | Autenticar usuário |
| GET / POST | `/fases` | Consultar / cadastrar fases |
| GET / POST | `/materias` | Consultar / cadastrar matérias |
| GET / POST | `/postagens` | Consultar / publicar postagens |
| GET / DELETE | `/postagens/{id}` | Consultar / excluir postagem |
| POST / DELETE | `/postagens/{id}/comentarios` e `/postagens/comentarios/{id}` | Comentar / excluir comentário |
| POST / DELETE | `/curtidas/postagens/{id}` | Curtir / remover curtida de postagem |
| POST / DELETE | `/curtidas/comentarios/{id}` | Curtir / remover curtida de comentário |

A interface Swagger está em `/swagger-ui.html`; o contrato OpenAPI JSON em `/v3/api-docs`.

## Exemplo de cadastro

```json
{
  "nome": "Maria da Silva",
  "email": "maria@ifsc.edu.br",
  "senha": "SenhaSegura123",
  "tipo": "ALUNO"
}
```

Use `POST /usuarios`. A senha deve possuir ao menos oito caracteres e é armazenada com BCrypt. Os tipos disponíveis estão no enum `TipoUsuario`.

## Arquitetura

A aplicação segue organização em camadas:

- **controller**: endpoints HTTP e conversão de requisições/respostas;
- **dto**: contratos de entrada e saída da API;
- **service**: regras de negócio e validações;
- **repository**: persistência com Spring Data JPA;
- **model**: entidades persistidas;
- **config**: configuração do Spring, dados iniciais e OpenAPI.

## Limitações do protótipo

A autenticação ainda é demonstrativa e não emite token nem implementa autorização por perfil. Antes de publicar o sistema, implementar autenticação/autorização (por exemplo Spring Security + JWT), validação de entrada mais completa, política de CORS por ambiente, migrações de banco (Flyway/Liquibase), paginação e logs/auditoria. Senhas novas são armazenadas com BCrypt; credenciais legadas em texto puro são convertidas para BCrypt após o primeiro login válido. O banco H2 em arquivo atende à demonstração; produção deve usar banco gerenciado e credenciais via variáveis de ambiente.

## Documentação da iteração

Ver [`docs/`](docs/): casos de uso, especificação REST, arquitetura, modelo relacional/ER, testes, [notificações WebSocket](docs/WEBSOCKET_NOTIFICACOES.md) e declaração de uso de IA.
