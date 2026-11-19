import { useEffect, useMemo, useRef, useState } from 'react'
import type { ForumMateria, ForumPhase } from '../../interfaces/forum'
import { getUserFacingError } from '../../services/api'

const MAX_FILES = 5
const MAX_FILE_SIZE = 10 * 1024 * 1024
const ALLOWED_FILE_TYPES = /\.(pdf|png|jpe?g)$/i

interface NewPostFormProps {
  phases: ForumPhase[]
  userId: number
  selectedPhaseId: number | null
  selectedMateriaId: number | null
  onSubmit: (payload: {
    titulo: string
    conteudo: string
    tipo: 'DUVIDA' | 'MATERIAL_ESTUDO'
    usuarioId: number
    faseId: number
    materiaId: number
    anonima: boolean
    arquivos: File[]
  }) => Promise<void>
  onCancel: () => void
}

export function NewPostForm({
  phases,
  userId,
  selectedPhaseId,
  selectedMateriaId,
  onSubmit,
  onCancel,
}: NewPostFormProps) {
  const [titulo, setTitulo] = useState('')
  const [conteudo, setConteudo] = useState('')
  const [tipo, setTipo] = useState<'DUVIDA' | 'MATERIAL_ESTUDO'>('DUVIDA')
  const [faseId, setFaseId] = useState<number>(selectedPhaseId ?? phases[0]?.id ?? 0)
  const initialPhase = phases.find((phase) => phase.id === (selectedPhaseId ?? phases[0]?.id))
  const [materiaId, setMateriaId] = useState<number>(
    selectedMateriaId && initialPhase?.materias.some((materia) => materia.id === selectedMateriaId)
      ? selectedMateriaId
      : initialPhase?.materias[0]?.id ?? 0,
  )
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [isAnonymous, setIsAnonymous] = useState(false)
  const [files, setFiles] = useState<File[]>([])
  const [errorMessage, setErrorMessage] = useState('')
  const onCancelRef = useRef(onCancel)

  useEffect(() => {
    onCancelRef.current = onCancel
  }, [onCancel])

  const selectedPhase = useMemo(() => phases.find((phase) => phase.id === faseId), [faseId, phases])

  useEffect(() => {
    if (!selectedPhase?.materias.some((materia) => materia.id === materiaId)) {
      setMateriaId(selectedPhase?.materias[0]?.id ?? 0)
    }
  }, [faseId, materiaId, selectedPhase])

  useEffect(() => {
    if (phases.length > 0 && !phases.some((phase) => phase.id === faseId)) {
      const initialPhaseId = phases.some((phase) => phase.id === selectedPhaseId)
        ? selectedPhaseId
        : phases[0].id
      setFaseId(initialPhaseId ?? phases[0].id)
    }
  }, [faseId, phases, selectedPhaseId])

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
        usuarioId: userId,
        faseId,
        materiaId,
        anonima: isAnonymous,
        arquivos: files,
      })
      setTitulo('')
      setConteudo('')
      setTipo('DUVIDA')
      setIsAnonymous(false)
      setFiles([])
      setFaseId(phases[0]?.id ?? 0)
      setMateriaId(phases[0]?.materias?.[0]?.id ?? 0)
      onCancel()
    } catch (error) {
      setErrorMessage(getUserFacingError(error, 'Não foi possível publicar. Tente novamente.'))
    } finally {
      setIsSubmitting(false)
    }
  }

  const materias: ForumMateria[] = selectedPhase?.materias ?? []
  const selectedMateria = materias.find((materia) => materia.id === materiaId)
  const typeLabel = tipo === 'DUVIDA' ? 'Dúvida' : 'Material de estudo'

  const handleFilesChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFiles = Array.from(event.target.files ?? [])
    event.target.value = ''
    setErrorMessage('')

    if (files.length + selectedFiles.length > MAX_FILES) {
      setErrorMessage(`Adicione no máximo ${MAX_FILES} arquivos por publicação.`)
      return
    }
    if (selectedFiles.some((file) => !ALLOWED_FILE_TYPES.test(file.name))) {
      setErrorMessage('Selecione arquivos PDF, PNG ou JPG.')
      return
    }
    if (selectedFiles.some((file) => file.size > MAX_FILE_SIZE)) {
      setErrorMessage('Cada arquivo deve ter no máximo 10 MB.')
      return
    }

    setFiles((current) => [...current, ...selectedFiles])
  }

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

          <label>
            <span>Descrição</span>
            <textarea
              value={conteudo}
              rows={4}
              onChange={(event) => setConteudo(event.target.value)}
              placeholder="Descreva sua dúvida ou compartilhe um material com a turma."
              required
            />
          </label>

          <section className="attachment-field" aria-labelledby="attachments-label">
            <div className="attachment-heading">
              <div>
                <strong id="attachments-label">Arquivos</strong>
                <small>PDF, PNG ou JPG · até 5 arquivos de 10 MB</small>
              </div>
              <label className="file-picker-button">
                Adicionar
                <input
                  type="file"
                  accept=".pdf,.png,.jpg,.jpeg,application/pdf,image/png,image/jpeg"
                  multiple
                  onChange={handleFilesChange}
                />
              </label>
            </div>
            {files.length > 0 && (
              <ul className="selected-files-list">
                {files.map((file, index) => (
                  <li key={`${file.name}-${file.lastModified}-${index}`}>
                    <span title={file.name}>{file.name}</span>
                    <small>{(file.size / (1024 * 1024)).toFixed(1)} MB</small>
                    <button
                      type="button"
                      onClick={() => setFiles((current) => current.filter((_, fileIndex) => fileIndex !== index))}
                      aria-label={`Remover ${file.name}`}
                    >
                      Remover
                    </button>
                  </li>
                ))}
              </ul>
            )}
          </section>

          <details className="post-options">
            <summary>
              <span>{typeLabel} · {selectedPhase?.numero ?? '—'}º período · {selectedMateria?.nome ?? 'Selecione uma matéria'}</span>
              <small>Opções da publicação</small>
            </summary>
            <div className="post-options-fields">
              <label>
                <span>Tipo</span>
                <select value={tipo} onChange={(event) => setTipo(event.target.value as 'DUVIDA' | 'MATERIAL_ESTUDO')}>
                  <option value="DUVIDA">Dúvida</option>
                  <option value="MATERIAL_ESTUDO">Material de estudo</option>
                </select>
              </label>
              <div className="field-grid two-columns">
                <label>
                  <span>Fase</span>
                  <select value={faseId} onChange={(event) => setFaseId(Number(event.target.value))}>
                    {phases.map((phase) => (
                      <option key={phase.id} value={phase.id}>
                        {phase.numero}º Período
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
              <label className="anonymous-option">
                <input
                  type="checkbox"
                  checked={isAnonymous}
                  onChange={(event) => setIsAnonymous(event.target.checked)}
                />
                <span>
                  <strong>Postar como anônimo</strong>
                  <small>Seu nome não será exibido nesta publicação.</small>
                </span>
              </label>
            </div>
          </details>

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
