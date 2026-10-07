import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import type { ForumPost } from '../../interfaces/forum'

interface QuestionCardProps {
  post: ForumPost
  isActive: boolean
  onOpenConversation: (post: ForumPost) => void
}

export function QuestionCard({ post, isActive, onOpenConversation }: QuestionCardProps) {
  const [liked, setLiked] = useState(Boolean(post.liked))
  const [likeCount, setLikeCount] = useState(post.commentsCount)

  useEffect(() => {
    setLiked(Boolean(post.liked))
    setLikeCount(post.commentsCount)
  }, [post.commentsCount, post.liked])

  const handleLikeToggle = () => {
    setLiked((current) => {
      const nextValue = !current
      setLikeCount((count) => count + (nextValue ? 1 : -1))
      return nextValue
    })
  }

  return (
    <article className={`question-card ${isActive ? 'active' : ''}`}>
      <div className="card-topline">
        <span className="card-label">{post.categoryLabel}</span>
      </div>

      <Link to={`/comentarios/${post.id}`} className="question-title-link">
        <h3>{post.title}</h3>
      </Link>

      <p>{post.description}</p>

      <div className="question-meta">
        <span>{post.author}</span>
        <span>•</span>
        <span>{likeCount} curtidas</span>
        <span>•</span>
        <span>{post.comments.length} respostas</span>
      </div>

      <div className="question-actions">
        <button type="button" className="primary-button small" onClick={() => onOpenConversation(post)}>
          Ver conversa
        </button>
        <button type="button" className="ghost-button small-link" onClick={() => onOpenConversation(post)}>
          Adicionar comentário
        </button>
        <button type="button" className={`inline-action ${liked ? 'active' : ''}`} onClick={handleLikeToggle}>
          {liked ? 'Curtido' : 'Curtir'} · {likeCount}
        </button>
        <Link to={`/comentarios/${post.id}`} className="ghost-button small-link">
          Comentários
        </Link>
      </div>
    </article>
  )
}
