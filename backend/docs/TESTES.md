# Estratégia e execução dos testes

Os testes existentes em `src/test/java` cobrem os controllers de usuário, fase, matéria, postagem e curtidas, além do carregamento da aplicação. Eles verificam respostas HTTP e fluxos integrados com Spring e persistência em banco H2.

## Executar

```bash
./gradlew test
```

O perfil de testes usa banco em memória (`src/test/resources/application.properties`) e `create-drop`, isolando os dados dos testes do banco local de desenvolvimento. O relatório HTML é gerado em `build/reports/tests/test/index.html`.

## Matriz mínima de verificação

| Área | Cenários a verificar |
|---|---|
| Usuário | cadastro válido, domínio de e-mail inválido, e-mail duplicado, login válido e inválido |
| Fases/matérias | listar, cadastrar, filtrar por fase e referenciar fase inexistente |
| Postagens | publicar válida, rejeitar campos obrigatórios ausentes, listar por filtros e consultar ID inexistente |
| Comentários | comentar em postagem existente e tratar IDs inválidos |
| Curtidas | curtir postagem/comentário existentes e tratar IDs inválidos |
| Persistência | reiniciar aplicação e confirmar que os dados do H2 em arquivo permanecem |

## Limitação de validação nesta entrega

A execução de `./gradlew test` neste ambiente não pôde ser concluída porque o Gradle Wrapper precisou baixar a distribuição Gradle 9.5.1 e o ambiente não tinha acesso à rede (`UnknownHostException`). Execute os testes localmente com JDK 21 e conexão para baixar as dependências. Não interprete a ausência de execução neste ambiente como aprovação dos testes.
