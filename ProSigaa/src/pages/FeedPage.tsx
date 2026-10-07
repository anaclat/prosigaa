import { useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import { ConversationPanel } from '../components/feed/ConversationPanel'
import { FiltersSidebar } from '../components/feed/FiltersSidebar'
import { NewPostForm } from '../components/feed/NewPostForm'
import { QuestionCard } from '../components/feed/QuestionCard'
import { Header } from '../components/layout/Header'
import { getFases, getForumPosts, createComment, createPost } from '../services/forumService'
import type { ForumPhase, ForumPost } from '../interfaces/forum'
import { curriculumPeriods } from '../data/curriculum'

export function FeedPage() {
  const [posts, setPosts] = useState<ForumPost[]>([])
  const [phases, setPhases] = useState<ForumPhase[]>([])
  const [selectedPhaseId, setSelectedPhaseId] = useState<number | null>(null)
  const [selectedMateriaId, setSelectedMateriaId] = useState<number | null>(null)
  const [filterType, setFilterType] = useState<'ALL' | 'DUVIDA' | 'MATERIAL_ESTUDO'>('ALL')
  const [selectedPost, setSelectedPost] = useState<ForumPost | null>(null)
  const [isCreatingPost, setIsCreatingPost] = useState(false)

  useEffect(() => {
    getForumPosts().then(setPosts)
    getFases().then(setPhases)
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
    setSelectedPost(created)
  }

  const handleCommentSubmit = async (content: string) => {
    if (!selectedPost) return

    const updatedPost = await createComment(selectedPost.id, {
      conteudo: content,
      usuarioId: 2,
    })

    setPosts((current) => current.map((post) => (post.id === updatedPost.id ? updatedPost : post)))
    setSelectedPost(updatedPost)
  }

  useEffect(() => {
    if (selectedPhaseId === null) {
      setSelectedMateriaId(null)
      return
    }

    const currentPhase = phases.find((phase) => phase.id === selectedPhaseId)
    if (!currentPhase || !currentPhase.materias.length) {
      setSelectedMateriaId(null)
      return
    }

    if (selectedMateriaId === null || !currentPhase.materias.some((materia) => materia.id === selectedMateriaId)) {
      setSelectedMateriaId(currentPhase.materias[0].id)
    }
  }, [phases, selectedMateriaId, selectedPhaseId])

  return (
    <div className="feed-page-shell">
      <Header />

      <main className="feed-layout">
        <div className="feed-left-column">
          <FiltersSidebar
            phases={phases}
            selectedPhaseId={selectedPhaseId}
            selectedMateriaId={selectedMateriaId}
            onSelectPhase={(phaseId) => {
              setSelectedPhaseId(phaseId)
            }}
            onSelectMateria={(materiaId) => {
              setSelectedMateriaId(materiaId)
            }}
          />
        </div>

        <div className="feed-center-column">
          <div className="feed-toolbar">
            <div className="feed-title-block">
              <h1>Sua turma, suas dúvidas.</h1>
            </div>
            <div className="toolbar-actions">
              <button type="button" className={`pill ${filterType === 'ALL' ? 'active' : ''}`} onClick={() => setFilterType('ALL')}>
                Todas
              </button>
              <button type="button" className={`pill ${filterType === 'DUVIDA' ? 'active' : ''}`} onClick={() => setFilterType('DUVIDA')}>
                Dúvidas
              </button>
              <button type="button" className={`pill ${filterType === 'MATERIAL_ESTUDO' ? 'active' : ''}`} onClick={() => setFilterType('MATERIAL_ESTUDO')}>
                Materiais
              </button>
              <button type="button" className="primary-button small" onClick={() => setIsCreatingPost(true)}>
                Nova postagem
              </button>
            </div>
          </div>

          {isCreatingPost && (
            <div className="composer-wrap">
              <NewPostForm
                phases={phases}
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
                  isActive={selectedPost?.id === post.id}
                  onOpenConversation={(nextPost) => setSelectedPost(nextPost)}
                />
              ))
            )}
          </div>
        </div>

        <aside className="feed-right-column">
          <div className="curriculum-sidebar">
            <h3>Estrutura curricular</h3>
            {curriculumPeriods.map((period) => (
              <div key={period.id} className="curriculum-card sidebar-card">
                <span>{period.nome}</span>
                <ul>
                  {period.disciplinas.map((disciplina) => (
                    <li key={disciplina}>{disciplina}</li>
                  ))}
                </ul>
              </div>
            ))}
          </div>

          {selectedPost ? (
            <ConversationPanel post={selectedPost} onClose={() => setSelectedPost(null)} onSubmitComment={handleCommentSubmit} />
          ) : (
            <>
              <div className="insight-card">
                <h3>Nesta semana</h3>
                <p>Todas as dúvidas do semestre, centralizadas por disciplina.</p>
              </div>
              <div className="insight-card">
                <h3>A melhor resposta ganha</h3>
                <p>Respostas validadas por professores recebem destaque visual.</p>
              </div>
            </>
          )}
          <div className="insight-card compact">
            <h3>Conexões rápidas</h3>
            <div className="quick-links">
              <Link to="/">Voltar para a apresentação</Link>
              <Link to="/feed">Atualizar feed</Link>
            </div>
          </div>
        </aside>
      </main>
    </div>
  )
}
