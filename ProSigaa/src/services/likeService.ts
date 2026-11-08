import { API_URL } from './api'

async function sendLike(
  target: 'postagens' | 'comentarios',
  contentId: string,
  userId: number,
): Promise<void> {
  const numericContentId = Number(contentId)
  if (!Number.isSafeInteger(numericContentId) || numericContentId <= 0) {
    throw new Error('Identificador do conteúdo inválido.')
  }

  const response = await fetch(
    `${API_URL}/curtidas/${target}/${numericContentId}`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ usuarioId: userId }),
    },
  )
  if (!response.ok) {
    const message = await response.text()
    throw new Error(message || 'Não foi possível registrar a curtida.')
  }
}

export function likePost(postId: string, userId: number): Promise<void> {
  return sendLike('postagens', postId, userId)
}

export function likeComment(commentId: string, userId: number): Promise<void> {
  return sendLike('comentarios', commentId, userId)
}

export function hasLikedContent(userId: number, target: 'postagem' | 'comentario', contentId: string): boolean {
  return window.localStorage.getItem(`prosigaa:curtida:${userId}:${target}:${contentId}`) === 'true'
}

export function rememberLike(userId: number, target: 'postagem' | 'comentario', contentId: string): void {
  window.localStorage.setItem(`prosigaa:curtida:${userId}:${target}:${contentId}`, 'true')
}
