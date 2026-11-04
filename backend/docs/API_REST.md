# API REST — ProSIGAA

A especificação interativa está disponível em `/swagger-ui.html`; o documento OpenAPI JSON em `/v3/api-docs`.

## Convenções

- Base local padrão: `http://localhost:8080`.
- Requisições e respostas usam JSON, exceto respostas `204 No Content`.
- Recursos canônicos no plural: `/usuarios`, `/postagens`, `/fases`, `/materias`, `/curtidas`.
- Identificadores de ações de escrita são enviados no corpo JSON, nunca em query string.
- `400`: entrada inválida/credenciais inválidas; `404`: recurso inexistente; `409`: conflito/integridade; `500`: erro inesperado.
- Respostas de criação usam `201 Created`; exclusões definitivas usam `204 No Content`.

## Endpoints

| Método | URI | Descrição | Sucesso |
|---|---|---|---|
| POST | `/usuarios` | Cadastrar usuário | 201 |
| GET | `/usuarios` | Listar dados públicos dos usuários | 200 |
| POST | `/usuarios/sessao` | Autenticar usuário | 200 |
| GET | `/fases` | Listar fases e matérias | 200 |
| POST | `/fases` | Cadastrar fase | 201 |
| GET | `/materias?faseId={id}` | Listar/filtrar matérias | 200 |
| POST | `/materias` | Cadastrar matéria | 201 |
| GET | `/postagens?faseId={id}&materiaId={id}` | Listar/filtrar postagens | 200 |
| GET | `/postagens/{id}` | Consultar postagem | 200 |
| POST | `/postagens` | Publicar postagem | 201 |
| DELETE | `/postagens/{id}` | Excluir postagem | 204 |
| POST | `/postagens/{id}/comentarios` | Comentar postagem | 201 |
| DELETE | `/postagens/comentarios/{comentarioId}` | Excluir comentário | 204 |
| POST | `/curtidas/postagens/{postagemId}` | Curtir postagem | 201 |
| DELETE | `/curtidas/postagens/{postagemId}` | Remover curtida da postagem | 200 |
| POST | `/curtidas/comentarios/{comentarioId}` | Curtir comentário | 201 |
| DELETE | `/curtidas/comentarios/{comentarioId}` | Remover curtida do comentário | 200 |

## Exemplos de payload

### Cadastro — `POST /usuarios`

```json
{
  "nome": "Maria Silva",
  "email": "maria@ifsc.edu.br",
  "senha": "SenhaSegura123",
  "tipo": "ALUNO"
}
```

Senha mínima de oito caracteres. A resposta não inclui senha nem hash.

### Login — `POST /usuarios/sessao`

```json
{ "email": "maria@ifsc.edu.br", "senha": "SenhaSegura123" }
```

### Curtir — `POST /curtidas/postagens/10`

```json
{ "usuarioId": 2 }
```

### Descurtir — `DELETE /curtidas/postagens/10`

```json
{ "usuarioId": 2 }
```

### Comentar — `POST /postagens/10/comentarios`

```json
{ "conteudo": "Ótima explicação!", "usuarioId": 2 }
```

## Compatibilidade

As rotas antigas no singular (`/postagem`, `/fase`, `/materia`, `/usuario`) e os caminhos `/usuario/cadastro` e `/usuario/login` permanecem como aliases temporários. Novos clientes devem utilizar exclusivamente as rotas plurais desta documentação. O antigo formato de curtida com `usuarioId` na query string não é mantido: envie o identificador no corpo JSON.

## Segurança e limitações

A senha é armazenada com BCrypt. O endpoint de listagem retorna apenas `UsuarioDTO` público. O protótipo ainda não possui JWT/sessão persistente nem autorização por papel; por isso, os identificadores de usuário no corpo representam uma simplificação didática, não uma autenticação segura para produção. A listagem de usuários deve ser protegida por autenticação e autorização antes de implantação pública.
