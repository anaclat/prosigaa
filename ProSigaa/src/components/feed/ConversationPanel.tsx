import { useEffect, useMemo, useState } from 'react'
import type { Comment, ForumPost } from '../../interfaces/forum'

interface ConversationPanelProps {
  post: ForumPost
  onClose: () => void
  onSubmitComment: (content: string) => Promise<void>
}

export function ConversationPanel({ post, onClose, onSubmitComment }: ConversationPanelProps) {
  const [draft, setDraft] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [commentReactions, setCommentReactions] = useState<Record<string, { liked: boolean; likes: number }>>({})

  const visibleComments = useMemo(() => post.comments ?? [], [post.comments])

  useEffect(() => {
    setCommentReactions((current) => {
      const nextState: Record<string, { liked: boolean; likes: number }> = { ...current }

      visibleComments.forEach((comment) => {
        const previous = current[comment.id]
        nextState[comment.id] = {
          liked: previous?.liked ?? Boolean(comment.liked),
          likes: previous?.likes ?? Number(comment.likes ?? 0),
        }
      })

      return nextState
    })
  }, [visibleComments])

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (!draft.trim()) return

    setIsSubmitting(true)
    try {
      await onSubmitComment(draft.trim())
      setDraft('')
    } finally {
      setIsSubmitting(false)
    }
  }

  const handleCommentLikeToggle = (comment: Comment) => {
    setCommentReactions((current) => {
      const existing = current[comment.id] ?? {
        liked: Boolean(comment.liked),
        likes: Number(comment.likes ?? 0),
      }

      const nextLiked = !existing.liked
      return {
        ...current,
        [comment.id]: {
          liked: nextLiked,
          likes: Math.max(0, existing.likes + (nextLiked ? 1 : -1)),
        },
      }
    })
  }

  return (
    <div className="conversation-panel">
      <div className="conversation-header">
        <div>
          <span className="card-label">{post.categoryLabel}</span>
          <h3>{post.title}</h3>
        </div>
        <button type="button" className="ghost-button tiny" onClick={onClose}>
          Fechar
        </button>
      </div>

      <p className="conversation-summary">{post.description}</p>

      <div className="conversation-list">
        {visibleComments.length === 0 ? (
          <div className="empty-conversation">Ainda não há respostas nesta conversa.</div>
        ) : (
          visibleComments.map((comment) => {
            const reaction = commentReactions[comment.id] ?? {
              liked: Boolean(comment.liked),
              likes: Number(comment.likes ?? 0),
            }
            return (
              <div key={comment.id} className={`comment-thread-item ${comment.isProfessor ? 'professor' : ''}`}>
                <div className="comment-avatar-badge">{comment.author.slice(0, 1).toUpperCase()}</div>
                <div className="comment-thread-body">
                  <div className="comment-thread-header">
                    <strong>{comment.author}</strong>
                    {comment.isProfessor && <span className="badge-verified">Professor</span>}
                  </div>
                  <p>{comment.message}</p>
                  <div className="comment-thread-footer">
                    <button
                      type="button"
                      className={`comment-action ${reaction.liked ? 'active' : ''}`}
                      onClick={() => handleCommentLikeToggle(comment)}
                    >
                      {reaction.liked ? 'Curtido' : 'Curtir'} · {reaction.likes}
                    </button>
                  </div>
                </div>
              </div>
            )
          })
        )}
      </div>

      <form className="comment-form" onSubmit={handleSubmit}>
        <textarea
          value={draft}
          onChange={(event) => setDraft(event.target.value)}
          rows={4}
          placeholder="Responder ou complementar a conversa..."
        />
        <div className="comment-form-actions">
          <button type="submit" className="primary-button small" disabled={isSubmitting || !draft.trim()}>
            {isSubmitting ? 'Enviando...' : 'Enviar resposta'}
          </button>
        </div>
      </form>
    </div>
  )
}
