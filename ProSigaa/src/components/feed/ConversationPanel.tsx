import { useMemo, useState } from 'react'
import type { ForumPost } from '../../interfaces/forum'

interface ConversationPanelProps {
  post: ForumPost
  onClose: () => void
  onSubmitComment: (content: string) => Promise<void>
}

export function ConversationPanel({ post, onClose, onSubmitComment }: ConversationPanelProps) {
  const [draft, setDraft] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  const visibleComments = useMemo(() => post.comments ?? [], [post.comments])

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
          visibleComments.map((comment) => (
            <div key={comment.id} className={`comment-thread-item ${comment.isProfessor ? 'professor' : ''}`}>
              <div className="comment-avatar-badge">{comment.author.slice(0, 1).toUpperCase()}</div>
              <div className="comment-thread-body">
                <div className="comment-thread-header">
                  <strong>{comment.author}</strong>
                  {comment.isProfessor && <span className="badge-verified">Professor</span>}
                </div>
                <p>{comment.message}</p>
              </div>
            </div>
          ))
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
