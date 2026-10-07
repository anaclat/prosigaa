import { forumPosts } from '../data/mockData'
import type { BackendPostDTO, ForumMateria, ForumPhase, ForumPost } from '../interfaces/forum'

const API_URL = (import.meta.env.VITE_API_URL as string | undefined) || 'http://localhost:8080'

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
    commentsCount: Number(post.curtidas ?? post.comentarios?.length ?? 0),
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
      likes: 0,
    })),
    phase: post.fase?.nome || 'Fase',
    phaseId: post.fase?.id,
    subject: post.materia?.nome || 'Matéria',
    subjectId: post.materia?.id,
    liked: false,
  }
}

export async function getForumPosts(): Promise<ForumPost[]> {
  try {
    const response = await fetch(`${API_URL}/postagem`)
    if (!response.ok) {
      throw new Error('Erro ao buscar posts')
    }
    const payload = (await response.json()) as BackendPostDTO[]
    return payload.map(normalizePost)
  } catch {
    return forumPosts
  }
}

export async function getForumPostById(postId: string): Promise<ForumPost | undefined> {
  try {
    const response = await fetch(`${API_URL}/postagem/${postId}`)
    if (!response.ok) {
      throw new Error('Post não encontrado')
    }
    const payload = (await response.json()) as BackendPostDTO
    return normalizePost(payload)
  } catch {
    return forumPosts.find((post) => post.id === postId)
  }
}

export async function getFases(): Promise<ForumPhase[]> {
  try {
    const response = await fetch(`${API_URL}/fase`)
    if (!response.ok) {
      throw new Error('Erro ao buscar fases')
    }
    const payload = (await response.json()) as { fases?: ForumPhase[] }
    return payload.fases ?? []
  } catch {
    return []
  }
}

export async function getMateriasByFase(faseId?: number): Promise<ForumMateria[]> {
  if (!faseId) return []

  try {
    const response = await fetch(`${API_URL}/materia?faseId=${faseId}`)
    if (!response.ok) {
      throw new Error('Erro ao buscar matérias')
    }
    const payload = (await response.json()) as ForumMateria[]
    return payload
  } catch {
    return []
  }
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
  const response = await fetch(`${API_URL}/postagem`, {
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
  const response = await fetch(`${API_URL}/postagem/${postId}/comentario`, {
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
