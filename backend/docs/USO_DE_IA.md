# Declaração de uso de inteligência artificial — ProSIGAA

## Ferramenta utilizada nesta revisão

- **Ferramenta:** ChatGPT, modelo GPT-6 (OpenAI).
- **Finalidade:** apoio à análise do feedback de avaliação, revisão da documentação técnica, sugestão/implementação de ajustes no código Java/Spring Boot e preparação de exemplos de API e diagramas.
- **Responsabilidade:** a ferramenta foi utilizada como assistente. A equipe continua responsável por compreender, revisar, testar e validar todo o conteúdo antes da entrega.

## Solicitações fornecidas nesta revisão

As instruções efetivamente fornecidas pelo usuário nesta conversa foram, em síntese:

1. “Dado o seguinte projeto em arquivo zip e o feedback do professor, forneça a lista de correções necessárias.”
2. Foi fornecido o feedback completo do professor apontando problemas em análise/modelagem, OpenAPI, códigos HTTP, rotas REST, operações de exclusão, consultas N+1, armazenamento de senhas e declaração de uso de IA.

A partir dessas solicitações, o trabalho foi direcionado para revisar os arquivos existentes, alinhar os casos de uso aos endpoints, substituir o diagrama de fluxo por notação UML de casos de uso, documentar operações REST e corrigir os pontos técnicos indicados.

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

## Exemplos de contribuição da IA

- Sugeriu a utilização de `BCryptPasswordEncoder` em vez de comparação/armazenamento de senha em texto puro.
- Sugeriu diferenciar `400 Bad Request`, `404 Not Found`, `409 Conflict` e `500 Internal Server Error` no tratamento centralizado.
- Sugeriu rotas de recursos no plural, envio de `usuarioId` no corpo JSON das operações de curtida e uso de `201 Created` em criações.
- Apoiou a redação dos fluxos alternativos dos casos de uso e a elaboração dos diagramas em PlantUML/Mermaid.
- Sugeriu `@BatchSize` e `hibernate.default_batch_fetch_size` para reduzir consultas N+1 nas associações acessadas ao montar os DTOs.

## Exemplos de criação humana

- Toda a estrutura inicial (como o projeto iria ser organizado) foi feito a mão
- A IA ajudou a criar o que faltava com base nos controllers/services/repositories/models já criados

## Validação humana

Foram testados manualmente os endpoints alterados por IA.
