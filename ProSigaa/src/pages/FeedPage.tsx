import { useCallback, useEffect, useMemo, useState } from 'react'
import { CommentDrawer } from '../components/feed/CommentDrawer'
import { FiltersSidebar } from '../components/feed/FiltersSidebar'
import { NewPostForm } from '../components/feed/NewPostForm'
import { QuestionCard } from '../components/feed/QuestionCard'
import { Header } from '../components/layout/Header'
import { getFases, getForumPosts, createComment, createPost } from '../services/forumService'
import { hasLikedContent, likeComment, likePost, rememberLike } from '../services/likeService'
import type { ForumPhase, ForumPost } from '../interfaces/forum'
import { fallbackForumPhases, getCurriculumPhases } from '../data/curriculum'

export function FeedPage() {
  const [posts, setPosts] = useState<ForumPost[]>([])
  const [phases, setPhases] = useState<ForumPhase[]>([])
  const [selectedPhaseId, setSelectedPhaseId] = useState<number | null>(null)
  const [selectedMateriaId, setSelectedMateriaId] = useState<number | null>(null)
  const [filterType, setFilterType] = useState<'ALL' | 'DUVIDA' | 'MATERIAL_ESTUDO'>('ALL')
  const [isCreatingPost, setIsCreatingPost] = useState(false)
  const [selectedPostId, setSelectedPostId] = useState<string | null>(null)
  const selectedPost = posts.find((post) => post.id === selectedPostId)
  const curriculumPhases = useMemo(() => getCurriculumPhases(phases), [phases])
  const currentUserId = Number(window.localStorage.getItem('usuarioId'))

  const closeComments = useCallback(() => setSelectedPostId(null), [])

  useEffect(() => {
    getForumPosts().then((loadedPosts) => {
      const userId = Number(window.localStorage.getItem('usuarioId'))
      setPosts(loadedPosts.map((post) => ({
        ...post,
        liked: hasLikedContent(userId, 'postagem', post.id),
        comments: post.comments.map((comment) => ({
          ...comment,
          liked: hasLikedContent(userId, 'comentario', comment.id),
        })),
      })))
    })
    getFases().then((loadedPhases) => {
      setPhases(loadedPhases.length ? loadedPhases : fallbackForumPhases)
    })
  }, [])

  const visiblePosts = useMemo(() => {
    return posts.filter((post) => {
      const phaseMatch = selectedPhaseId === null || post.phaseId === selectedPhaseId
      const subjectMatch = selectedMateriaId === null || post.subjectId === selectedMateriaId
      const typeMatch = filterType === 'ALL' || post.category === filterType
      return phaseMatch && subjectMatch && typeMatch
    })
  }, [filterType, posts, selectedMateriaId, selectedPhaseId])

  const handleNewPost = async (payload: {
    titulo: string
    conteudo: string
    tipo: 'DUVIDA' | 'MATERIAL_ESTUDO'
    usuarioId: number
    faseId: number
    materiaId: number
  }) => {
    const created = await createPost(payload)
    setPosts((current) => [created, ...current])
  }

  const handleCommentSubmit = async (postId: string, content: string) => {
    if (!Number.isSafeInteger(currentUserId) || currentUserId <= 0) {
      throw new Error('Sua sessão expirou. Entre novamente para comentar.')
    }

    const updatedPost = await createComment(postId, {
      conteudo: content,
      usuarioId: currentUserId,
    })

    setPosts((current) => current.map((post) => post.id === updatedPost.id
      ? {
          ...updatedPost,
          liked: post.liked,
          comments: updatedPost.comments.map((comment) => {
            const previousComment = post.comments.find((item) => item.id === comment.id)
            return previousComment
              ? { ...comment, liked: previousComment.liked, likes: Math.max(comment.likes ?? 0, previousComment.likes ?? 0) }
              : comment
          }),
        }
      : post))
  }

  const handleLikePost = async (postId: string) => {
    if (!Number.isSafeInteger(currentUserId) || currentUserId <= 0) {
      throw new Error('Sua sessão expirou. Entre novamente para curtir.')
    }

    const post = posts.find((item) => item.id === postId)
    if (!post || post.liked) return

    setPosts((current) => current.map((item) => item.id === postId
      ? { ...item, liked: true, likesCount: item.likesCount + 1 }
      : item))
    try {
      await likePost(postId, currentUserId)
      rememberLike(currentUserId, 'postagem', postId)
    } catch (error) {
      setPosts((current) => current.map((item) => item.id === postId
        ? { ...item, liked: false, likesCount: Math.max(0, item.likesCount - 1) }
        : item))
      throw error
    }
  }

  const handleLikeComment = async (commentId: string) => {
    if (!Number.isSafeInteger(currentUserId) || currentUserId <= 0) {
      throw new Error('Sua sessão expirou. Entre novamente para curtir.')
    }

    const post = posts.find((item) => item.comments.some((comment) => comment.id === commentId))
    const comment = post?.comments.find((item) => item.id === commentId)
    if (!post || !comment || comment.liked) return

    setPosts((current) => current.map((item) => item.id === post.id
      ? {
          ...item,
          comments: item.comments.map((reply) => reply.id === commentId
            ? { ...reply, liked: true, likes: (reply.likes ?? 0) + 1 }
            : reply),
        }
      : item))
    try {
      await likeComment(commentId, currentUserId)
      rememberLike(currentUserId, 'comentario', commentId)
    } catch (error) {
      setPosts((current) => current.map((item) => item.id === post.id
        ? {
            ...item,
            comments: item.comments.map((reply) => reply.id === commentId
              ? { ...reply, liked: false, likes: Math.max(0, (reply.likes ?? 0) - 1) }
              : reply),
          }
        : item))
      throw error
    }
  }

  return (
    <div className="feed-page-shell">
      <Header />

      <main className="feed-layout">
        <div className="feed-left-column">
          <FiltersSidebar
            phases={curriculumPhases}
            selectedPhaseId={selectedPhaseId}
            selectedMateriaId={selectedMateriaId}
            filterType={filterType}
            onSelectPhase={(phaseId) => {
              setSelectedPhaseId(phaseId)
              setSelectedMateriaId(null)
            }}
            onSelectMateria={(materiaId) => {
              setSelectedMateriaId(materiaId)
            }}
            onFilterTypeChange={setFilterType}
          />
        </div>

        <div className="feed-center-column">
          <div className="feed-toolbar">
            <div className="feed-title-block">
              <h1>Sua turma, suas dúvidas.</h1>
            </div>
            <div className="toolbar-actions">
              <button type="button" className="primary-button small" onClick={() => setIsCreatingPost(true)}>
                Nova postagem
              </button>
            </div>
          </div>

          {isCreatingPost && (
            <div className="composer-wrap">
              <NewPostForm
                phases={curriculumPhases}
                userId={currentUserId}
                onSubmit={handleNewPost}
                onCancel={() => setIsCreatingPost(false)}
              />
            </div>
          )}

          <div className="post-list">
            {visiblePosts.length === 0 ? (
              <div className="empty-state">Nenhuma conversa encontrada para os filtros selecionados.</div>
            ) : (
              visiblePosts.map((post) => (
                <QuestionCard
                  key={post.id}
                  post={post}
                  onOpenComments={setSelectedPostId}
                  onLike={handleLikePost}
                />
              ))
            )}
          </div>
        </div>

      </main>
      {selectedPost && (
        <CommentDrawer
          post={selectedPost}
          onClose={closeComments}
          onSubmitComment={handleCommentSubmit}
          onLikeComment={handleLikeComment}
        />
      )}
    </div>
  )
}
