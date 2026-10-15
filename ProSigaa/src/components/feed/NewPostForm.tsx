import { useEffect, useMemo, useRef, useState } from 'react'
import type { ForumMateria, ForumPhase } from '../../interfaces/forum'

interface NewPostFormProps {
  phases: ForumPhase[]
  onSubmit: (payload: {
    titulo: string
    conteudo: string
    tipo: 'DUVIDA' | 'MATERIAL_ESTUDO'
    usuarioId: number
    faseId: number
    materiaId: number
  }) => Promise<void>
  onCancel: () => void
}

const DEFAULT_USER_ID = 2

export function NewPostForm({ phases, onSubmit, onCancel }: NewPostFormProps) {
  const [titulo, setTitulo] = useState('')
  const [conteudo, setConteudo] = useState('')
  const [tipo, setTipo] = useState<'DUVIDA' | 'MATERIAL_ESTUDO'>('DUVIDA')
  const [faseId, setFaseId] = useState<number>(0)
  const [materiaId, setMateriaId] = useState<number>(0)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [isAnonymous, setIsAnonymous] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')
  const onCancelRef = useRef(onCancel)

  useEffect(() => {
    onCancelRef.current = onCancel
  }, [onCancel])

  const selectedPhase = useMemo(
    () => phases.find((phase) => phase.id === faseId) ?? phases[0],
    [faseId, phases],
  )

  useEffect(() => {
    if (!selectedPhase) {
      setFaseId(0)
      setMateriaId(0)
      return
    }

    if (!selectedPhase.materias?.length) {
      setMateriaId(0)
      return
    }

    if (!selectedPhase.materias.some((materia) => materia.id === materiaId)) {
      setMateriaId(selectedPhase.materias[0].id)
    }
  }, [materiaId, selectedPhase])

  useEffect(() => {
    if (phases.length > 0 && !faseId) {
      setFaseId(phases[0].id)
    }
  }, [faseId, phases])

  useEffect(() => {
    const previousOverflow = document.body.style.overflow
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === 'Escape') onCancelRef.current()
    }

    document.body.style.overflow = 'hidden'
    window.addEventListener('keydown', handleKeyDown)
    return () => {
      document.body.style.overflow = previousOverflow
      window.removeEventListener('keydown', handleKeyDown)
    }
  }, [])

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (!titulo.trim() || !conteudo.trim() || !faseId || !materiaId) return

    setIsSubmitting(true)
    setErrorMessage('')
    try {
      await onSubmit({
        titulo: titulo.trim(),
        conteudo: conteudo.trim(),
        tipo,
        usuarioId: DEFAULT_USER_ID,
        faseId,
        materiaId,
      })
      setTitulo('')
      setConteudo('')
      setTipo('DUVIDA')
      setIsAnonymous(false)
      setFaseId(phases[0]?.id ?? 0)
      setMateriaId(phases[0]?.materias?.[0]?.id ?? 0)
      onCancel()
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'Não foi possível publicar. Tente novamente.')
    } finally {
      setIsSubmitting(false)
    }
  }

  const materias: ForumMateria[] = selectedPhase?.materias ?? []

  return (
    <div
      className="post-modal-backdrop"
      role="presentation"
      onMouseDown={(event) => {
        if (event.target === event.currentTarget) onCancel()
      }}
    >
      <section
        className="new-post-panel post-modal"
        role="dialog"
        aria-modal="true"
        aria-labelledby="new-post-title"
      >
        <div className="panel-header-row">
          <div>
            <span className="modal-eyebrow">COMPARTILHE COM A TURMA</span>
            <h2 id="new-post-title">Nova publicação</h2>
          </div>
          <button type="button" className="modal-close-button" onClick={onCancel} aria-label="Fechar">
            ×
          </button>
        </div>

        <form className="new-post-form" onSubmit={handleSubmit}>
          <label>
            <span>Título</span>
            <input
              value={titulo}
              onChange={(event) => setTitulo(event.target.value)}
              placeholder="Ex.: Dúvida sobre estruturas condicionais"
              required
              autoFocus
            />
          </label>

          <div className="field-grid two-columns">
            <label>
              <span>Tipo</span>
              <select value={tipo} onChange={(event) => setTipo(event.target.value as 'DUVIDA' | 'MATERIAL_ESTUDO')}>
                <option value="DUVIDA">Dúvida</option>
                <option value="MATERIAL_ESTUDO">Material de estudo</option>
              </select>
            </label>
            <label>
              <span>Fase</span>
              <select value={faseId} onChange={(event) => setFaseId(Number(event.target.value))}>
                {phases.map((phase) => (
                  <option key={phase.id} value={phase.id}>
                    {phase.nome}
                  </option>
                ))}
              </select>
            </label>
          </div>
          <label>
            <span>Matéria</span>
            <select value={materiaId} onChange={(event) => setMateriaId(Number(event.target.value))}>
              {materias.map((materia) => (
                <option key={materia.id} value={materia.id}>
                  {materia.nome}
                </option>
              ))}
            </select>
          </label>

          <label>
            <span>Descrição</span>
            <textarea
              value={conteudo}
              rows={5}
              onChange={(event) => setConteudo(event.target.value)}
              placeholder="Descreva sua dúvida ou compartilhe um material com a turma."
              required
            />
          </label>

          <label className="anonymous-option">
            <input
              type="checkbox"
              checked={isAnonymous}
              onChange={(event) => setIsAnonymous(event.target.checked)}
            />
            <span>
              <strong>Postar como anônimo</strong>
              <small>A API atual ainda não salva essa preferência.</small>
            </span>
          </label>

          {errorMessage && <p className="form-error" role="alert">{errorMessage}</p>}

          <div className="form-actions">
            <button type="button" className="ghost-button" onClick={onCancel}>
              Cancelar
            </button>
            <button type="submit" className="primary-button" disabled={isSubmitting || !faseId || !materiaId}>
              {isSubmitting ? 'Publicando...' : 'Publicar'}
            </button>
          </div>
        </form>
      </section>
    </div>
  )
}
