import { useState } from 'react'
import type { ForumPost } from '../../interfaces/forum'

interface QuestionCardProps {
  post: ForumPost
  onOpenComments: (postId: string) => void
  onLike: (postId: string) => Promise<void>
  onDelete: (postId: string) => Promise<void>
}

export function QuestionCard({ post, onOpenComments, onLike, onDelete }: QuestionCardProps) {
  const [isLiking, setIsLiking] = useState(false)
  const [isDeleting, setIsDeleting] = useState(false)
  const [isMenuOpen, setIsMenuOpen] = useState(false)
  const [likeError, setLikeError] = useState('')
  const [deleteError, setDeleteError] = useState('')

  const handleLike = async () => {
    if (isLiking) return

    setIsLiking(true)
    setLikeError('')
    try {
      await onLike(post.id)
    } catch (error) {
      setLikeError(error instanceof Error ? error.message : 'Não foi possível curtir a publicação.')
    } finally {
      setIsLiking(false)
    }
  }

  const handleDelete = async () => {
    setIsMenuOpen(false)
    if (isDeleting || !window.confirm('Excluir esta postagem? Ela será removida do feed de todos.')) return

    setIsDeleting(true)
    setDeleteError('')
    try {
      await onDelete(post.id)
    } catch (error) {
      setDeleteError(error instanceof Error ? error.message : 'Não foi possível excluir a postagem.')
      setIsDeleting(false)
    }
  }

  return (
    <article className="question-card">
      <div className="card-topline">
        <span className="card-label">{post.categoryLabel}</span>
        <div className="overflow-menu">
          <button
            type="button"
            className="overflow-menu-trigger"
            aria-label={`Mais opções para a postagem ${post.title}`}
            aria-haspopup="menu"
            aria-expanded={isMenuOpen}
            onClick={() => setIsMenuOpen((open) => !open)}
          >
            <span aria-hidden="true">⋯</span>
          </button>
          {isMenuOpen && (
            <div className="overflow-menu-items" role="menu">
              <button
                type="button"
                role="menuitem"
                className="overflow-menu-delete"
                onClick={() => void handleDelete()}
                disabled={isDeleting}
              >
                {isDeleting ? 'Excluindo...' : 'Excluir postagem'}
              </button>
            </div>
          )}
        </div>
      </div>

      <div className="question-title-link">
        <h3>{post.title}</h3>
      </div>

      <p>{post.description}</p>

      <div className="question-meta">
        <span>{post.author}</span>
        <span aria-hidden="true">·</span>
        <span>{post.time}</span>
        <span aria-hidden="true">·</span>
        <span>{post.comments.length} respostas</span>
      </div>

      <div className="question-actions">
        <button
          type="button"
          className={`inline-action like-action ${post.liked ? 'active' : ''}`}
          onClick={handleLike}
          disabled={isLiking}
          aria-pressed={Boolean(post.liked)}
          aria-label={`${post.liked ? 'Descurtir' : 'Curtir'} publicação, ${post.likesCount} curtidas`}
        >
          <svg aria-hidden="true" viewBox="0 0 24 24">
            <path d="M20.8 4.6a5.5 5.5 0 0 0-7.8 0L12 5.7l-1.1-1.1a5.5 5.5 0 0 0-7.8 7.8l1.1 1.1L12 21l7.8-7.5 1.1-1.1a5.5 5.5 0 0 0-.1-7.8Z" />
          </svg>
          {post.likesCount}
        </button>
        <button
          type="button"
          className="inline-action comment-toggle"
          onClick={() => onOpenComments(post.id)}
          aria-label={`Abrir ${post.comments.length} comentários de ${post.title}`}
        >
          Comentários ({post.comments.length})
        </button>
      </div>
      {likeError && <p className="form-error" role="alert">{likeError}</p>}
      {deleteError && <p className="form-error" role="alert">{deleteError}</p>}
    </article>
  )
}
