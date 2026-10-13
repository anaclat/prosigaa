# Especificação da API REST

A especificação OpenAPI é gerada a partir da aplicação e dos DTOs. Com o backend em execução:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Todos os corpos são JSON salvo indicação em contrário. IDs são numéricos (`Long`). Respostas de erro do `ApiExceptionHandler` seguem `{ "erro": "mensagem" }` para `IllegalArgumentException`.

## Recursos e operações

| Recurso | Operação | Entrada relevante | Resposta esperada |
|---|---|---|---|
| Usuário | `GET /usuario` | — | Lista de usuários sem senha |
| Usuário | `POST /usuario/cadastro` | `CadastroUsuarioDTO` | Usuário criado (DTO público) |
| Usuário | `POST /usuario/login` | `LoginDTO` | Usuário autenticado (DTO público) |
| Fase | `GET /fase` | — | Fases com matérias |
| Fase | `POST /fase` | `CadastroFaseDTO` | Fase cadastrada |
| Matéria | `GET /materia?faseId={id}` | filtro opcional | Lista de matérias |
| Matéria | `POST /materia` | `CadastroMateriaDTO` | Matéria cadastrada |
| Postagem | `GET /postagem?faseId={id}&materiaId={id}` | filtros opcionais | Lista de postagens |
| Postagem | `GET /postagem/{id}` | path ID | Detalhe com comentários e curtidas |
| Postagem | `POST /postagem` | `CadastroPostagemDTO` | Postagem criada |
| Comentário | `POST /postagem/{id}/comentario` | `CadastroComentarioDTO` | Postagem atualizada |
| Curtida | `POST /curtida/postagem/{postagemId}?usuarioId={id}` | IDs | Mensagem de confirmação |
| Curtida | `POST /curtida/comentario/{comentarioId}?usuarioId={id}` | IDs | Mensagem de confirmação |

## Códigos HTTP

- `200 OK`: consulta ou operação concluída.
- `400 Bad Request`: validação/regra de negócio representada por `IllegalArgumentException`.
- `404 Not Found`: o comportamento atual de IDs inexistentes é convertido em 400 pelo handler global; recomenda-se separar `EntityNotFoundException` e mapear para 404 em evolução futura.

## Exemplo de publicação

`POST /postagem`

```json
{
  "titulo": "Dúvida sobre listas encadeadas",
  "conteudo": "Alguém pode indicar material de estudo?",
  "urlImagem": null,
  "tipo": "DUVIDA",
  "usuarioId": 2,
  "faseId": 1,
  "materiaId": 1
}
```

Os valores de enum e campos obrigatórios devem ser confirmados no Swagger/DTOs da versão executada.
