import { useEffect, useMemo, useState } from 'react'
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

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (!titulo.trim() || !conteudo.trim() || !faseId || !materiaId) return

    setIsSubmitting(true)
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
      setFaseId(phases[0]?.id ?? 0)
      setMateriaId(phases[0]?.materias?.[0]?.id ?? 0)
      onCancel()
    } finally {
      setIsSubmitting(false)
    }
  }

  const materias: ForumMateria[] = selectedPhase?.materias ?? []

  return (
    <div className="new-post-panel">
      <div className="panel-header-row">
        <h3>Nova postagem</h3>
        <button type="button" className="ghost-button tiny" onClick={onCancel}>
          Fechar
        </button>
      </div>

      <form className="new-post-form" onSubmit={handleSubmit}>
        <div className="field-grid">
          <label>
            <span>Título</span>
            <input value={titulo} onChange={(event) => setTitulo(event.target.value)} placeholder="Ex.: Dúvida sobre estruturas condicionais" />
          </label>

          <label>
            <span>Tipo</span>
            <select value={tipo} onChange={(event) => setTipo(event.target.value as 'DUVIDA' | 'MATERIAL_ESTUDO')}>
              <option value="DUVIDA">Dúvida</option>
              <option value="MATERIAL_ESTUDO">Material de estudo</option>
            </select>
          </label>
        </div>

        <div className="field-grid two-columns">
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
        </div>

        <label>
          <span>Conteúdo</span>
          <textarea value={conteudo} rows={6} onChange={(event) => setConteudo(event.target.value)} placeholder="Descreva sua dúvida, material ou contexto." />
        </label>

        <div className="form-actions">
          <button type="button" className="ghost-button" onClick={onCancel}>
            Cancelar
          </button>
          <button type="submit" className="primary-button" disabled={isSubmitting || !faseId || !materiaId}>
            {isSubmitting ? 'Publicando...' : 'Publicar'}
          </button>
        </div>
      </form>
    </div>
  )
}
