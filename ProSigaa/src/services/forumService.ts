import type { BackendPostDTO, ForumMateria, ForumPhase, ForumPost } from '../interfaces/forum'
import { API_URL } from './api'

function normalizePost(post: BackendPostDTO): ForumPost {
  const category = post.tipo === 'DUVIDA' ? 'DUVIDA' : 'MATERIAL_ESTUDO'

  return {
    id: String(post.id),
    title: post.titulo,
    category,
    description: post.conteudo,
    author: post.usuario?.nome || 'Aluno',
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
  }
}

export async function getForumPosts(): Promise<ForumPost[]> {
  const response = await fetch(`${API_URL}/postagens`)
  if (!response.ok) {
    throw new Error((await response.text()) || 'Erro ao buscar postagens.')
  }
  const payload = (await response.json()) as BackendPostDTO[]
  return payload.map(normalizePost)
}

export async function getForumPostById(postId: string): Promise<ForumPost | undefined> {
  const response = await fetch(`${API_URL}/postagens/${postId}`)
  if (!response.ok) {
    throw new Error((await response.text()) || 'Postagem não encontrada.')
  }
  const payload = (await response.json()) as BackendPostDTO
  return normalizePost(payload)
}

export async function getFases(): Promise<ForumPhase[]> {
  const response = await fetch(`${API_URL}/fases`)
  if (!response.ok) {
    throw new Error((await response.text()) || 'Erro ao buscar fases e matérias.')
  }
  const payload = (await response.json()) as { fases: ForumPhase[] }
  return payload.fases
}

export async function getMateriasByFase(faseId?: number): Promise<ForumMateria[]> {
  if (!faseId) return []

  const response = await fetch(`${API_URL}/materias?faseId=${faseId}`)
  if (!response.ok) {
    throw new Error((await response.text()) || 'Erro ao buscar matérias.')
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
  urlImagem?: string | null
}): Promise<ForumPost> {
  const response = await fetch(`${API_URL}/postagens`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload),
  })

  if (!response.ok) {
    const text = await response.text()
    throw new Error(text || 'Não foi possível criar a postagem.')
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
    const text = await response.text()
    throw new Error(text || 'Não foi possível enviar a resposta.')
  }

  const data = (await response.json()) as BackendPostDTO
  return normalizePost(data)
}
