import { API_URL, readApiError } from './api'

async function sendLike(
  target: 'postagens' | 'comentarios',
  contentId: string,
  userId: number,
  method: 'POST' | 'DELETE',
): Promise<void> {
  const numericContentId = Number(contentId)
  if (!Number.isSafeInteger(numericContentId) || numericContentId <= 0) {
    throw new Error('Identificador do conteúdo inválido.')
  }
  if (!Number.isSafeInteger(userId) || userId <= 0) {
    throw new Error('Identificador do usuário inválido.')
  }

  const url = method === 'DELETE'
    ? `${API_URL}/curtidas/${target}/${numericContentId}/usuarios/${userId}`
    : `${API_URL}/curtidas/${target}/${numericContentId}`

  const response = await fetch(url, method === 'POST'
    ? {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ usuarioId: userId }),
      }
    : { method })
  if (!response.ok) {
    throw new Error(await readApiError(response, 'Não foi possível registrar sua curtida.'))
  }
}

export function likePost(postId: string, userId: number): Promise<void> {
  return sendLike('postagens', postId, userId, 'POST')
}

export function likeComment(commentId: string, userId: number): Promise<void> {
  return sendLike('comentarios', commentId, userId, 'POST')
}

export function unlikePost(postId: string, userId: number): Promise<void> {
  return sendLike('postagens', postId, userId, 'DELETE')
}

export function unlikeComment(commentId: string, userId: number): Promise<void> {
  return sendLike('comentarios', commentId, userId, 'DELETE')
}

export async function getLikedPostIds(userId: number): Promise<string[]> {
  if (!Number.isSafeInteger(userId) || userId <= 0) {
    throw new Error('Sua sessão expirou. Entre novamente para ver suas curtidas.')
  }

  const response = await fetch(`${API_URL}/curtidas/postagens?usuarioId=${encodeURIComponent(String(userId))}`)
  if (!response.ok) {
    throw new Error(await readApiError(response, 'Não foi possível carregar suas postagens curtidas.'))
  }

  const postIds = await response.json() as number[]
  return postIds.map(String)
}

export function hasLikedContent(userId: number, target: 'postagem' | 'comentario', contentId: string): boolean {
  return window.localStorage.getItem(`prosigaa:curtida:${userId}:${target}:${contentId}`) === 'true'
}

export function rememberLike(userId: number, target: 'postagem' | 'comentario', contentId: string): void {
  window.localStorage.setItem(`prosigaa:curtida:${userId}:${target}:${contentId}`, 'true')
}

export function forgetLike(userId: number, target: 'postagem' | 'comentario', contentId: string): void {
  window.localStorage.removeItem(`prosigaa:curtida:${userId}:${target}:${contentId}`)
}
