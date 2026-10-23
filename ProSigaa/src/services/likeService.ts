import { API_URL } from './api'

async function sendLike(
  target: 'postagem' | 'comentario',
  contentId: string,
  userId: number,
): Promise<void> {
  const numericContentId = Number(contentId)
  if (!Number.isSafeInteger(numericContentId) || numericContentId <= 0) {
    return
  }

  const response = await fetch(
    `${API_URL}/curtida/${target}/${numericContentId}?usuarioId=${userId}`,
    { method: 'POST' },
  )
  if (!response.ok) {
    const message = await response.text()
    throw new Error(message || 'Não foi possível registrar a curtida.')
  }
}

export function likePost(postId: string, userId: number): Promise<void> {
  return sendLike('postagem', postId, userId)
}

export function likeComment(commentId: string, userId: number): Promise<void> {
  return sendLike('comentario', commentId, userId)
}

export function hasLikedContent(userId: number, target: 'postagem' | 'comentario', contentId: string): boolean {
  return window.localStorage.getItem(`prosigaa:curtida:${userId}:${target}:${contentId}`) === 'true'
}

export function rememberLike(userId: number, target: 'postagem' | 'comentario', contentId: string): void {
  window.localStorage.setItem(`prosigaa:curtida:${userId}:${target}:${contentId}`, 'true')
}
