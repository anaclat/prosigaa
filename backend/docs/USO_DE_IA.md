# Declaração de uso de inteligência artificial — ProSIGAA

## Ferramentas e participação

Usamos **Copilot**, **Gemini** e **ChatGPT** como ferramentas de apoio em diferentes etapas do projeto. Não mantivemos um registro completo que associe cada ferramenta a um modelo ou versão específica, a cada prompt e a cada linha de código. Por isso, descrevemos abaixo as contribuições que conseguimos identificar, sem atribuir autoria de trechos específicos quando não temos essa informação.

Também usamos o assistente integrado ao Copilot SDK no VS Code nesta interação para analisar o feedback, sugerir e implementar alterações e revisar a documentação. A equipe continua responsável por compreender, revisar, testar e validar o código e a documentação antes da entrega.

## Solicitações e objetivos

No trabalho anterior, fornecemos o feedback do professor sobre análise e modelagem, OpenAPI, códigos HTTP, rotas REST, operações de exclusão, consultas N+1, armazenamento de senhas e declaração de uso de IA. Também solicitamos uma lista de correções necessárias.

Nesta interação, pedimos ajuda para organizar as correções em etapas e commits separados, escrever as mensagens de commit em português, deixar a revisão de senhas para o final, acrescentar enums ao diagrama de classes e continuar o cronograma. Também pedimos que a declaração de IA preservasse as informações anteriores e fosse escrita como uma fala da equipe.

Não registramos os prompts exatos usados em todas as etapas anteriores. Quando não temos o texto literal, descrevemos o objetivo da solicitação em vez de apresentá-lo como citação.

## Arquivos do projeto examinados ou alterados com assistência de IA

- `build.gradle` — inclusão da dependência para BCrypt.
- `src/main/java/com/ifsc/prosigaa/config/PasswordConfig.java` — configuração de `PasswordEncoder`.
- `src/main/java/com/ifsc/prosigaa/config/DadosIniciais.java` — hash das credenciais criadas pelos dados iniciais.
- `src/main/java/com/ifsc/prosigaa/model/Usuario.java` — verificação BCrypt e migração de credencial legada no primeiro login válido.
- `src/main/java/com/ifsc/prosigaa/model/Postagem.java`, `Comentario.java`, `Fase.java` e `Materia.java` — carregamento em lote das associações.
- `src/main/java/com/ifsc/prosigaa/service/UsuarioService.java`, `PostagemService.java`, `ComentarioService.java` e `CurtidaService.java` — validações, tratamento de recursos inexistentes e operações de exclusão.
- `src/main/java/com/ifsc/prosigaa/repository/CurtidaPostagemRepository.java`, `CurtidaComentarioRepository.java` e `ComentarioRepository.java` — métodos de remoção.
- `src/main/java/com/ifsc/prosigaa/controller/ApiExceptionHandler.java`, `UsuarioController.java`, `PostagemController.java`, `CurtidaController.java`, `FaseController.java` e `MateriaController.java` — respostas HTTP, rotas REST e descrições OpenAPI.
- `src/main/java/com/ifsc/prosigaa/dto/` — descrições de schemas e DTO para corpo das operações de curtida.
- `src/main/resources/application.properties` — configuração de busca em lote.
- `docs/CASOS_DE_USO.md`, `docs/ARQUITETURA_E_MODELO.md` e `docs/API_REST.md` — revisão da documentação funcional, estrutural e do contrato HTTP.
- `docs/USO_DE_IA.md` — esta declaração.
- `ProSigaa/src/pages/AuthPage.tsx`, `ProSigaa/src/services/forumService.ts` e `ProSigaa/src/services/likeService.ts` — alinhamento do frontend às rotas REST plurais e envio de `usuarioId` no corpo das requisições de curtida.
- `backend/src/main/java/com/ifsc/prosigaa/controller/` — documentação OpenAPI das respostas de sucesso e erro.
- `backend/src/main/java/com/ifsc/prosigaa/dto/ApiErrorDTO.java` e DTOs de cadastro — schema de erro comum e exemplos de campos para o OpenAPI.
- `backend/src/test/java/com/ifsc/prosigaa/controller/` e `backend/src/test/java/com/ifsc/prosigaa/integration/ApiIntegracaoTest.java` — testes de rotas canônicas, status HTTP, erros, exclusões e documentação OpenAPI.
- `backend/docs/CASOS_DE_USO.md` e `backend/docs/ARQUITETURA_E_MODELO.md` — detalhamento de casos de uso e revisão dos diagramas, incluindo enums.

No commit `b524328`, usamos IA como apoio para adicionar tratamento de exceções, implementar hash de senhas, atualizar o README e adicionar testes. O commit contém mudanças relacionadas a esses tópicos, mas não identifica quais trechos foram produzidos com IA. Não temos registrados a ferramenta específica, o modelo, os prompts ou a lista exata de arquivos dessa contribuição.

## Exemplos de contribuição da IA

- Usamos sugestões de IA para configurar `BCryptPasswordEncoder` em vez de comparar ou armazenar senhas em texto puro.
- Com apoio de IA, diferenciamos respostas `400 Bad Request`, `404 Not Found`, `409 Conflict` e `500 Internal Server Error` no tratamento centralizado.
- Com apoio de IA, revisamos as rotas de recursos para usar plural, enviamos `usuarioId` no corpo JSON das operações de curtida e usamos `201 Created` nas criações.
- Usamos IA como apoio para redigir fluxos alternativos dos casos de uso e elaborar diagramas em PlantUML/Mermaid.
- Com apoio de IA, configuramos `@BatchSize` e `hibernate.default_batch_fetch_size` para reduzir consultas N+1 ao montar DTOs.
- Nesta interação, alinhamos as chamadas do frontend às rotas plurais da API e ao envio de dados de curtida no corpo JSON.
- Documentamos respostas de erro no OpenAPI usando um DTO comum e ampliamos os testes do contrato documentado com apoio de IA.
- Expandimos os casos de uso, revisamos diagramas UML e acrescentamos ao diagrama de classes os enums existentes, com apoio de IA.
- Criamos, com apoio de IA, uma verificação de integração para medir as consultas do endpoint de listagem de postagens com associações carregadas em lote.

## Exemplos de criação humana

- Criamos manualmente a estrutura inicial e organizamos o projeto.
- Também usamos IA para ajudar a completar partes com base nos controllers, services, repositories e models que já existiam.

## Validação humana

Testamos manualmente endpoints alterados com apoio de IA em etapas anteriores. Nesta interação, executamos o build do frontend e a suíte backend: 26 testes passaram, sem falhas. Não concluímos o lint do frontend porque o projeto não possui um arquivo de configuração `eslint.config.*` compatível com ESLint 9.

Como equipe, somos responsáveis por revisar e validar as contribuições antes da entrega. Devemos complementar esta declaração se recuperarmos informações mais precisas sobre ferramentas, modelos, prompts e arquivos das etapas anteriores.
