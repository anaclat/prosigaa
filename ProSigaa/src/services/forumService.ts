import type { BackendPostDTO, ForumAttachment, ForumMateria, ForumPhase, ForumPost } from '../interfaces/forum'
import { API_URL, readApiError } from './api'

function normalizePost(post: BackendPostDTO): ForumPost {
  const category = post.tipo === 'DUVIDA' ? 'DUVIDA' : 'MATERIAL_ESTUDO'

  return {
    id: String(post.id),
    title: post.titulo,
    category,
    description: post.conteudo,
    author: post.anonima ? 'Anônimo' : post.usuario?.nome || 'Aluno',
    authorId: post.usuario?.id ? Number(post.usuario.id) : undefined,
    time: new Date(post.criadoEm).toLocaleString('pt-BR', {
      day: '2-digit',
      month: 'short',
      hour: '2-digit',
      minute: '2-digit',
    }),
    categoryLabel: `${post.materia?.nome || 'Matéria'} · ${category === 'DUVIDA' ? 'Dúvida' : 'Material'}`,
    likesCount: Number(post.curtidas ?? 0),
    comments: (post.comentarios || []).map((comment) => ({
      id: String(comment.id),
      author: comment.usuario?.nome || 'Usuário',
      authorId: comment.usuario?.id ? Number(comment.usuario.id) : undefined,
      message: comment.conteudo,
      time: new Date(comment.criadoEm).toLocaleString('pt-BR', {
        day: '2-digit',
        month: 'short',
        hour: '2-digit',
        minute: '2-digit',
      }),
      isProfessor: comment.usuario?.tipo === 'PROFESSOR',
      liked: false,
      likes: Number(comment.curtidas ?? 0),
    })),
    phase: post.fase?.nome || 'Fase',
    phaseId: post.fase?.id,
    subject: post.materia?.nome || 'Matéria',
    subjectId: post.materia?.id,
    liked: false,
    attachments: (post.anexos || []).map((attachment): ForumAttachment => ({
      id: String(attachment.id),
      name: attachment.nome,
      contentType: attachment.tipoConteudo,
      size: attachment.tamanhoBytes,
      url: `${API_URL}${attachment.url}`,
    })),
  }
}

export async function getForumPosts(): Promise<ForumPost[]> {
  const response = await fetch(`${API_URL}/postagens`)
  if (!response.ok) {
    throw new Error(await readApiError(response, 'Não foi possível carregar as postagens.'))
  }
  const payload = (await response.json()) as BackendPostDTO[]
  return payload.map(normalizePost)
}

export async function getForumPostById(postId: string): Promise<ForumPost | undefined> {
  const response = await fetch(`${API_URL}/postagens/${postId}`)
  if (!response.ok) {
    throw new Error(await readApiError(response, 'Não foi possível encontrar essa postagem.'))
  }
  const payload = (await response.json()) as BackendPostDTO
  return normalizePost(payload)
}

export async function getFases(): Promise<ForumPhase[]> {
  const response = await fetch(`${API_URL}/fases`)
  if (!response.ok) {
    throw new Error(await readApiError(response, 'Não foi possível carregar as fases e matérias.'))
  }
  const payload = (await response.json()) as { fases: ForumPhase[] }
  return payload.fases
}

export async function getMateriasByFase(faseId?: number): Promise<ForumMateria[]> {
  if (!faseId) return []

  const response = await fetch(`${API_URL}/materias?faseId=${faseId}`)
  if (!response.ok) {
    throw new Error(await readApiError(response, 'Não foi possível carregar as matérias.'))
  }
  const payload = (await response.json()) as ForumMateria[]
  return payload
}

export async function createPost(payload: {
  titulo: string
  conteudo: string
  tipo: 'DUVIDA' | 'MATERIAL_ESTUDO'
  usuarioId: number
  faseId: number
  materiaId: number
  anonima: boolean
  urlImagem?: string | null
  arquivos?: File[]
}): Promise<ForumPost> {
  const { arquivos = [], ...dados } = payload
  let response: Response

  if (arquivos.length > 0) {
    const formData = new FormData()
    formData.append('dados', new Blob([JSON.stringify(dados)], { type: 'application/json' }))
    arquivos.forEach((arquivo) => formData.append('arquivos', arquivo))
    response = await fetch(`${API_URL}/postagens`, { method: 'POST', body: formData })
  } else {
    response = await fetch(`${API_URL}/postagens`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(dados),
    })
  }

  if (!response.ok) {
    throw new Error(await readApiError(response, 'Não foi possível publicar. Tente novamente.'))
  }

  const data = (await response.json()) as BackendPostDTO
  return normalizePost(data)
}

export async function createComment(postId: string | number, payload: { conteudo: string; usuarioId: number }): Promise<ForumPost> {
  const response = await fetch(`${API_URL}/postagens/${postId}/comentarios`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })

  if (!response.ok) {
    throw new Error(await readApiError(response, 'Não foi possível enviar sua resposta.'))
  }

  const data = (await response.json()) as BackendPostDTO
  return normalizePost(data)
}

export async function deletePost(postId: string, userId: number): Promise<void> {
  const response = await fetch(`${API_URL}/postagens/${postId}?usuarioId=${encodeURIComponent(String(userId))}`, { method: 'DELETE' })
  if (!response.ok) {
    throw new Error(await readApiError(response, 'Não foi possível excluir a postagem.'))
  }
}

export async function deleteComment(commentId: string, userId: number): Promise<void> {
  const response = await fetch(`${API_URL}/postagens/comentarios/${commentId}?usuarioId=${encodeURIComponent(String(userId))}`, {
    method: 'DELETE',
  })

  if (!response.ok) {
    throw new Error(await readApiError(response, 'Não foi possível excluir o comentário.'))
  }
}
