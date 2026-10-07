import type { ForumMateria, ForumPhase } from '../../interfaces/forum'

interface FiltersSidebarProps {
  phases: ForumPhase[]
  selectedPhaseId: number | null
  selectedMateriaId: number | null
  onSelectPhase: (phaseId: number | null) => void
  onSelectMateria: (materiaId: number | null) => void
}

export function FiltersSidebar({
  phases,
  selectedPhaseId,
  selectedMateriaId,
  onSelectPhase,
  onSelectMateria,
}: FiltersSidebarProps) {
  const activePhase = phases.find((phase) => phase.id === selectedPhaseId) ?? phases[0]
  const materias: ForumMateria[] = activePhase?.materias ?? []

  return (
    <aside className="filters-panel">
      <div className="mini-label">MINHAS FASES</div>
      <div className="subject-list">
        <button
          type="button"
          className={`subject-item ${selectedPhaseId === null ? 'active' : ''}`}
          onClick={() => onSelectPhase(null)}
        >
          Todas as fases
        </button>
        {phases.map((phase) => (
          <button
            key={phase.id}
            type="button"
            className={`subject-item ${selectedPhaseId === phase.id ? 'active' : ''}`}
            onClick={() => onSelectPhase(phase.id)}
          >
            {phase.nome}
          </button>
        ))}
      </div>

      {materias.length > 0 && (
        <>
          <div className="mini-label secondary">MATÉRIAS</div>
          <div className="subject-list">
            <button
              type="button"
              className={`subject-item ${selectedMateriaId === null ? 'active' : ''}`}
              onClick={() => onSelectMateria(null)}
            >
              Todas as matérias
            </button>
            {materias.map((materia) => (
              <button
                key={materia.id}
                type="button"
                className={`subject-item ${selectedMateriaId === materia.id ? 'active' : ''}`}
                onClick={() => onSelectMateria(materia.id)}
              >
                {materia.nome}
              </button>
            ))}
          </div>
        </>
      )}
    </aside>
  )
}
