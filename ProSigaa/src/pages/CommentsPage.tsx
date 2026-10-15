import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { Header } from '../components/layout/Header'
import { getForumPostById } from '../services/forumService'
import type { ForumPost } from '../interfaces/forum'

export function CommentsPage() {
  const { postId } = useParams()
  const [post, setPost] = useState<ForumPost | undefined>()

  useEffect(() => {
    if (!postId) return
    getForumPostById(postId).then(setPost)
  }, [postId])

  if (!post) {
    return <div className="page-empty">Carregando conversa...</div>
  }

  return (
    <div className="feed-page-shell">
      <Header />

      <main className="comments-layout">
        <div className="comments-content">
          <div className="comments-card featured">
            <span className="card-label">{post.categoryLabel}</span>
            <h1>{post.title}</h1>
            <p>{post.description}</p>
            <div className="question-meta">
              <span>{post.author}</span>
              <span>•</span>
              <span>{post.likesCount} curtidas</span>
            </div>
          </div>

          <div className="comment-thread">
            {post.comments.map((comment) => (
              <div key={comment.id} className={`comment-thread-item ${comment.isProfessor ? 'professor' : ''}`}>
                <div className="comment-avatar-badge">{comment.author.slice(0, 1).toUpperCase()}</div>
                <div className="comment-thread-body">
                  <div className="comment-thread-header">
                    <strong>{comment.author}</strong>
                    {comment.isProfessor && (
                      <span className="badge-verified">Garantido pelo Professor</span>
                    )}
                  </div>
                  <p>{comment.message}</p>
                </div>
              </div>
            ))}
          </div>
        </div>

        <aside className="comments-sidebar">
          <div className="comments-sidebar-header">
            <h2>Comentários</h2>
            <Link to="/feed" className="close-link">Fechar</Link>
          </div>

          {post.comments.map((comment) => (
            <div key={`${comment.id}-sidebar`} className="comment-mini-card">
              <h4>{comment.author}</h4>
              <p>{comment.message}</p>
            </div>
          ))}
        </aside>
      </main>
    </div>
  )
}
