import { useState } from 'react'
import type { ForumPost } from '../../interfaces/forum'

interface QuestionCardProps {
  post: ForumPost
  onOpenComments: (postId: string) => void
  onLike: (postId: string) => Promise<void>
}

export function QuestionCard({ post, onOpenComments, onLike }: QuestionCardProps) {
  const [isLiking, setIsLiking] = useState(false)
  const [likeError, setLikeError] = useState('')

  const handleLike = async () => {
    if (post.liked || isLiking) return

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

  return (
    <article className="question-card">
      <div className="card-topline">
        <span className="card-label">{post.categoryLabel}</span>
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
          disabled={post.liked || isLiking}
          aria-pressed={Boolean(post.liked)}
          aria-label={`${post.liked ? 'Curtida' : 'Curtir'} publicação, ${post.likesCount} curtidas`}
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
    </article>
  )
}
