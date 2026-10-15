import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import type { ForumPost } from '../../interfaces/forum'

interface QuestionCardProps {
  post: ForumPost
  onSubmitComment: (postId: string, content: string) => Promise<void>
}

export function QuestionCard({ post, onSubmitComment }: QuestionCardProps) {
  const [liked, setLiked] = useState(Boolean(post.liked))
  const [likeCount, setLikeCount] = useState(post.likesCount)
  const [commentsOpen, setCommentsOpen] = useState(false)
  const [draft, setDraft] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')

  useEffect(() => {
    setLiked(Boolean(post.liked))
    setLikeCount(post.likesCount)
  }, [post.likesCount, post.liked])

  const handleLikeToggle = () => {
    const nextLiked = !liked
    setLiked(nextLiked)
    setLikeCount((count) => Math.max(0, count + (nextLiked ? 1 : -1)))
  }

  const handleCommentSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (!draft.trim()) return

    setIsSubmitting(true)
    setErrorMessage('')
    try {
      await onSubmitComment(post.id, draft.trim())
      setDraft('')
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'Não foi possível enviar o comentário.')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <article className="question-card">
      <div className="card-topline">
        <span className="card-label">{post.categoryLabel}</span>
      </div>

      <Link to={`/comentarios/${post.id}`} className="question-title-link">
        <h3>{post.title}</h3>
      </Link>

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
          className={`inline-action like-action ${liked ? 'active' : ''}`}
          onClick={handleLikeToggle}
          aria-pressed={liked}
          aria-label={`${liked ? 'Descurtir' : 'Curtir'} publicação, ${likeCount} curtidas`}
        >
          <svg aria-hidden="true" viewBox="0 0 24 24">
            <path d="M20.8 4.6a5.5 5.5 0 0 0-7.8 0L12 5.7l-1.1-1.1a5.5 5.5 0 0 0-7.8 7.8l1.1 1.1L12 21l7.8-7.5 1.1-1.1a5.5 5.5 0 0 0-.1-7.8Z" />
          </svg>
          {likeCount}
        </button>
        <button
          type="button"
          className="inline-action comment-toggle"
          onClick={() => setCommentsOpen((open) => !open)}
          aria-expanded={commentsOpen}
          aria-controls={`comments-${post.id}`}
        >
          {commentsOpen ? 'Fechar respostas' : `Comentários (${post.comments.length})`}
        </button>
        <Link to={`/comentarios/${post.id}`} className="post-detail-link">Abrir conversa</Link>
      </div>

      {commentsOpen && (
        <section className="expanded-comments" id={`comments-${post.id}`} aria-label={`Comentários em ${post.title}`}>
          {post.comments.length > 0 ? (
            <div className="expanded-comment-list">
              {post.comments.map((comment) => (
                <article className="expanded-comment" key={comment.id}>
                  <div className="expanded-comment-heading">
                    <strong>{comment.author}</strong>
                    {comment.isProfessor && (
                      <span className="professor-guarantee">Garantido pelo Professor</span>
                    )}
                    <time>{comment.time}</time>
                  </div>
                  <p>{comment.message}</p>
                </article>
              ))}
            </div>
          ) : (
            <p className="no-comments">Seja a primeira pessoa a responder.</p>
          )}

          <form className="inline-comment-form" onSubmit={handleCommentSubmit}>
            <label className="visually-hidden" htmlFor={`reply-${post.id}`}>Escreva um comentário</label>
            <input
              id={`reply-${post.id}`}
              value={draft}
              onChange={(event) => setDraft(event.target.value)}
              placeholder="Escreva um comentário..."
              maxLength={1000}
            />
            <button type="submit" className="comment-send-button" disabled={isSubmitting || !draft.trim()}>
              {isSubmitting ? 'Enviando...' : 'Enviar'}
            </button>
          </form>
          {errorMessage && <p className="form-error" role="alert">{errorMessage}</p>}
        </section>
      )}
    </article>
  )
}
