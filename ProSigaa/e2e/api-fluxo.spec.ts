import { expect, test, type APIRequestContext, type APIResponse } from '@playwright/test';

const dados = {
  fase: { nome: 'Fase 1 - E2E', numero: 1 },
  materia: { nome: 'Programação Web - E2E', codigo: 'PROG-WEB-E2E-V1' },
  usuario: {
    nome: 'Aluno Teste E2E v1',
    email: 'aluno.e2e.v1@ifsc.edu.br',
    senha: 'SenhaFixa123',
    tipo: 'ALUNO',
  },
  postagem: {
    titulo: 'Dúvida Teste E2E',
    conteudo: 'Postagem persistente criada pela suíte de testes E2E.',
    tipo: 'DUVIDA',
  },
  comentario: 'Comentário de validação E2E v1.',
};

interface FaseApi {
  id: number;
  nome: string;
  numero: number;
}

interface MateriaApi {
  id: number;
  nome: string;
  codigo: string;
}

interface UsuarioApi {
  id: number;
  email: string;
  tipo: string;
}

interface ComentarioApi {
  conteudo: string;
}

interface PostagemApi {
  id: number;
  titulo: string;
  conteudo: string;
  tipo: string;
  fase: FaseApi;
  materia: MateriaApi;
  comentarios: ComentarioApi[];
  curtidas: number;
}

async function lerJson<T>(response: APIResponse): Promise<T> {
  return (await response.json()) as T;
}

async function obterOuCriarFase(request: APIRequestContext): Promise<FaseApi> {
  const consulta = await request.get('fases');
  expect(consulta.status()).toBe(200);

  const resultado = await lerJson<{ fases: FaseApi[] }>(consulta);
  const existente = resultado.fases.find(
    (fase) => fase.nome === dados.fase.nome && fase.numero === dados.fase.numero,
  );

  if (existente) {
    return existente;
  }

  const cadastro = await request.post('fases', { data: dados.fase });
  expect(cadastro.status()).toBe(201);
  return lerJson<FaseApi>(cadastro);
}

async function obterOuCriarMateria(
  request: APIRequestContext,
  faseId: number,
): Promise<MateriaApi> {
  const consulta = await request.get('materias', { params: { faseId } });
  expect(consulta.status()).toBe(200);

  const materias = await lerJson<MateriaApi[]>(consulta);
  const existente = materias.find(
    (materia) =>
      materia.nome === dados.materia.nome && materia.codigo === dados.materia.codigo,
  );

  if (existente) {
    return existente;
  }

  const cadastro = await request.post('materias', {
    data: { ...dados.materia, faseId },
  });
  expect(cadastro.status()).toBe(201);
  return lerJson<MateriaApi>(cadastro);
}

test('cadastra, autentica e interage com uma postagem persistente', async ({ request }) => {
  // Reutiliza a fase e matéria se o teste já tiver sido executado anteriormente.
  const fase = await obterOuCriarFase(request);
  const materia = await obterOuCriarMateria(request, fase.id);

  // Não remove dados no final. Em reexecuções, o cadastro duplicado é seguido de login.
  const cadastroUsuario = await request.post('usuarios', {
    data: dados.usuario,
  });
  expect([201, 409]).toContain(cadastroUsuario.status());

  const login = await request.post('usuarios/login', {
    data: {
      email: dados.usuario.email,
      senha: dados.usuario.senha,
    },
  });
  expect(login.status()).toBe(200);

  const usuario = await lerJson<UsuarioApi>(login);
  expect(usuario.email).toBe(dados.usuario.email);
  expect(usuario.tipo).toBe('ALUNO');

  const criacaoPostagem = await request.post('postagens', {
    data: {
      ...dados.postagem,
      usuarioId: usuario.id,
      faseId: fase.id,
      materiaId: materia.id,
    },
  });
  expect(criacaoPostagem.status()).toBe(201);

  const postagem = await lerJson<PostagemApi>(criacaoPostagem);
  expect(postagem.id).toBeGreaterThan(0);
  expect(postagem.titulo).toBe(dados.postagem.titulo);
  expect(postagem.fase.id).toBe(fase.id);
  expect(postagem.materia.id).toBe(materia.id);

  const criacaoComentario = await request.post(
    `postagens/${postagem.id}/comentario`,
    {
      data: {
        conteudo: dados.comentario,
        usuarioId: usuario.id,
      },
    },
  );
  expect(criacaoComentario.status()).toBe(201);

  const curtida = await request.post(`curtidas/postagens/${postagem.id}`, {
    data: { usuarioId: usuario.id },
  });
  expect(curtida.status()).toBe(201);

  // Lê a postagem novamente para confirmar que postagem, comentário e curtida persistiram.
  const consultaPostagem = await request.get(`postagens/${postagem.id}`);
  expect(consultaPostagem.status()).toBe(200);

  const postagemPersistida = await lerJson<PostagemApi>(consultaPostagem);
  expect(postagemPersistida.titulo).toBe(dados.postagem.titulo);
  expect(postagemPersistida.comentarios).toEqual(
    expect.arrayContaining([
      expect.objectContaining({ conteudo: dados.comentario }),
    ]),
  );
  expect(postagemPersistida.curtidas).toBeGreaterThanOrEqual(1);
});
