import { useEffect, useState } from 'react'
import type { ForumPost } from '../../interfaces/forum'

interface QuestionCardProps {
  post: ForumPost
  onOpenComments: (postId: string) => void
}

export function QuestionCard({ post, onOpenComments }: QuestionCardProps) {
  const [liked, setLiked] = useState(Boolean(post.liked))
  const [likeCount, setLikeCount] = useState(post.likesCount)

  useEffect(() => {
    setLiked(Boolean(post.liked))
    setLikeCount(post.likesCount)
  }, [post.likesCount, post.liked])

  const handleLikeToggle = () => {
    const nextLiked = !liked
    setLiked(nextLiked)
    setLikeCount((count) => Math.max(0, count + (nextLiked ? 1 : -1)))
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
          onClick={() => onOpenComments(post.id)}
          aria-label={`Abrir ${post.comments.length} comentários de ${post.title}`}
        >
          Comentários ({post.comments.length})
        </button>
      </div>
    </article>
  )
}
