import type { ForumPost } from '../../interfaces/forum'

interface CommentPanelProps {
  post: ForumPost
}

export function CommentPanel({ post }: CommentPanelProps) {
  return (
    <aside className="comment-panel">
      <div className="comment-header">
        <h3>Comentários</h3>
        <button type="button" className="comment-close">Fechar</button>
      </div>

      <div className="comment-list">
        {post.comments.map((comment) => (
          <div key={comment.id} className={`comment-item ${comment.isProfessor ? 'is-professor' : ''}`}>
            <div className="comment-author-row">
              <strong>{comment.author}</strong>
              {comment.isProfessor && <span className="professor-tag">Autoridade</span>}
            </div>
            <p>{comment.message}</p>
          </div>
        ))}
      </div>
    </aside>
  )
}
