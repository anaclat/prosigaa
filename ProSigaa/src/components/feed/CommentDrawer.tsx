import { useEffect, useRef, useState, type FormEvent } from 'react'
import type { ForumPost } from '../../interfaces/forum'

interface CommentDrawerProps {
  post: ForumPost
  currentUserId: number
  onClose: () => void
  onSubmitComment: (postId: string, content: string) => Promise<void>
  onLikeComment: (commentId: string) => Promise<void>
  onDeleteComment: (commentId: string) => Promise<void>
}

export function CommentDrawer({ post, currentUserId, onClose, onSubmitComment, onLikeComment, onDeleteComment }: CommentDrawerProps) {
  const [draft, setDraft] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')
  const [likingCommentIds, setLikingCommentIds] = useState<string[]>([])
  const [deletingCommentIds, setDeletingCommentIds] = useState<string[]>([])
  const [openMenuCommentId, setOpenMenuCommentId] = useState<string | null>(null)
  const [likeError, setLikeError] = useState('')
  const [deleteError, setDeleteError] = useState('')
  const inputRef = useRef<HTMLInputElement>(null)

  useEffect(() => {
    inputRef.current?.focus()

    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === 'Escape') onClose()
    }

    document.addEventListener('keydown', handleKeyDown)
    document.body.classList.add('drawer-open')
    return () => {
      document.removeEventListener('keydown', handleKeyDown)
      document.body.classList.remove('drawer-open')
    }
  }, [onClose])

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    const content = draft.trim()
    if (!content) return

    setIsSubmitting(true)
    setErrorMessage('')
    try {
      await onSubmitComment(post.id, content)
      setDraft('')
      inputRef.current?.focus()
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'Não foi possível enviar o comentário.')
    } finally {
      setIsSubmitting(false)
    }
  }

  const handleLikeComment = async (commentId: string) => {
    if (likingCommentIds.includes(commentId)) return

    setLikingCommentIds((current) => [...current, commentId])
    setLikeError('')
    try {
      await onLikeComment(commentId)
    } catch (error) {
      setLikeError(error instanceof Error ? error.message : 'Não foi possível curtir o comentário.')
    } finally {
      setLikingCommentIds((current) => current.filter((id) => id !== commentId))
    }
  }

  const handleDeleteComment = async (commentId: string) => {
    setOpenMenuCommentId(null)
    if (deletingCommentIds.includes(commentId) || !window.confirm('Excluir este comentário?')) return

    setDeletingCommentIds((current) => [...current, commentId])
    setDeleteError('')
    try {
      await onDeleteComment(commentId)
    } catch (error) {
      setDeleteError(error instanceof Error ? error.message : 'Não foi possível excluir o comentário.')
      setDeletingCommentIds((current) => current.filter((id) => id !== commentId))
    }
  }

  return (
    <div className="comment-drawer-backdrop" onMouseDown={(event) => {
      if (event.target === event.currentTarget) onClose()
    }}>
      <aside
        className="comment-drawer"
        role="dialog"
        aria-modal="true"
        aria-labelledby="comment-drawer-title"
      >
        <header className="comment-drawer-header">
          <div>
            <span className="card-label">{post.categoryLabel}</span>
            <h2 id="comment-drawer-title">Comentários</h2>
          </div>
          <button type="button" className="drawer-close-button" onClick={onClose} aria-label="Fechar comentários">
            <span aria-hidden="true">×</span>
          </button>
        </header>

        <div className="comment-drawer-content">
          <article className="drawer-original-post">
            <h3>{post.title}</h3>
            <p>{post.description}</p>
            <div className="question-meta">
              <span>{post.author}</span>
              <span aria-hidden="true">·</span>
              <span>{post.time}</span>
            </div>
          </article>

          <section className="drawer-comment-list" aria-label="Lista de comentários">
            {post.comments.length === 0 ? (
              <p className="no-comments">Ainda não há comentários. Comece a conversa.</p>
            ) : (
              post.comments.map((comment) => {
                const isCommentOwner = Number(comment.authorId) === currentUserId

                return (
                  <article className="drawer-comment" key={comment.id}>
                    <div className="expanded-comment-heading">
                      <strong>{comment.author}</strong>
                      {comment.isProfessor && (
                        <span className="professor-guarantee">Garantido pelo Professor</span>
                      )}
                      <time>{comment.time}</time>
                      {isCommentOwner && (
                        <div className="overflow-menu">
                          <button
                            type="button"
                            className="overflow-menu-trigger"
                            aria-label={`Mais opções para o comentário de ${comment.author}`}
                            aria-haspopup="menu"
                            aria-expanded={openMenuCommentId === comment.id}
                            onClick={() => setOpenMenuCommentId((current) => current === comment.id ? null : comment.id)}
                          >
                            <span aria-hidden="true">⋯</span>
                          </button>
                          {openMenuCommentId === comment.id && (
                            <div className="overflow-menu-items" role="menu">
                              <button
                                type="button"
                                role="menuitem"
                                className="overflow-menu-delete"
                                onClick={() => void handleDeleteComment(comment.id)}
                                disabled={deletingCommentIds.includes(comment.id)}
                              >
                                {deletingCommentIds.includes(comment.id) ? 'Excluindo...' : 'Excluir comentário'}
                              </button>
                            </div>
                          )}
                        </div>
                      )}
                    </div>
                    <p>{comment.message}</p>
                    <div className="drawer-comment-actions">
                      <button
                        type="button"
                        className={`inline-action like-action comment-like-action ${comment.liked ? 'active' : ''}`}
                        onClick={() => void handleLikeComment(comment.id)}
                        disabled={likingCommentIds.includes(comment.id)}
                        aria-pressed={Boolean(comment.liked)}
                        aria-label={`${comment.liked ? 'Descurtir' : 'Curtir'} comentário, ${comment.likes ?? 0} curtidas`}
                      >
                        <svg aria-hidden="true" viewBox="0 0 24 24">
                          <path d="M20.8 4.6a5.5 5.5 0 0 0-7.8 0L12 5.7l-1.1-1.1a5.5 5.5 0 0 0-7.8 7.8l1.1 1.1L12 21l7.8-7.5 1.1-1.1a5.5 5.5 0 0 0-.1-7.8Z" />
                        </svg>
                        {comment.likes ?? 0}
                      </button>
                    </div>
                  </article>
                )
              })
            )}
          </section>
        </div>

        {likeError && <p className="comment-drawer-error" role="alert">{likeError}</p>}
        {deleteError && <p className="comment-drawer-error" role="alert">{deleteError}</p>}
        <form className="comment-drawer-form" onSubmit={handleSubmit}>
          <label className="visually-hidden" htmlFor="drawer-comment-input">Escreva um comentário</label>
          <input
            ref={inputRef}
            id="drawer-comment-input"
            value={draft}
            onChange={(event) => setDraft(event.target.value)}
            placeholder="Escreva um comentário..."
            maxLength={1000}
          />
          <button type="submit" className="comment-send-button" disabled={isSubmitting || !draft.trim()}>
            {isSubmitting ? 'Enviando...' : 'Enviar'}
          </button>
          {errorMessage && <p className="form-error" role="alert">{errorMessage}</p>}
        </form>
      </aside>
    </div>
  )
}
