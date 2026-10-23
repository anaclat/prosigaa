import { useEffect, useState } from 'react'
import type { ForumMateria, ForumPhase } from '../../interfaces/forum'

type FilterType = 'ALL' | 'DUVIDA' | 'MATERIAL_ESTUDO'

interface FiltersSidebarProps {
  phases: ForumPhase[]
  selectedPhaseId: number | null
  selectedMateriaId: number | null
  filterType: FilterType
  onSelectPhase: (phaseId: number | null) => void
  onSelectMateria: (materiaId: number | null) => void
  onFilterTypeChange: (filterType: FilterType) => void
}

export function FiltersSidebar({
  phases,
  selectedPhaseId,
  selectedMateriaId,
  filterType,
  onSelectPhase,
  onSelectMateria,
  onFilterTypeChange,
}: FiltersSidebarProps) {
  const [expandedPhaseId, setExpandedPhaseId] = useState<number | null>(selectedPhaseId)

  useEffect(() => {
    if (selectedPhaseId !== null) setExpandedPhaseId(selectedPhaseId)
  }, [selectedPhaseId])

  const filters: { label: string; value: FilterType }[] = [
    { label: 'Todas', value: 'ALL' },
    { label: 'Dúvidas', value: 'DUVIDA' },
    { label: 'Materiais', value: 'MATERIAL_ESTUDO' },
  ]

  return (
    <aside className="filters-panel">
      <div className="mini-label">TIPO DE POSTAGEM</div>
      <div className="filter-chips" role="group" aria-label="Filtrar tipo de postagem">
        {filters.map((filter) => (
          <button
            type="button"
            key={filter.value}
            className={`pill ${filterType === filter.value ? 'active' : ''}`}
            onClick={() => onFilterTypeChange(filter.value)}
            aria-pressed={filterType === filter.value}
          >
            {filter.label}
          </button>
        ))}
      </div>

      <div className="mini-label secondary phase-label">FASES E MATÉRIAS</div>
      <div className="subject-list phase-list">
        <button
          type="button"
          className={`subject-item ${selectedPhaseId === null ? 'active' : ''}`}
          onClick={() => onSelectPhase(null)}
        >
          Todas as fases
        </button>
        {phases.map((phase) => {
          const isExpanded = expandedPhaseId === phase.id
          const materias: ForumMateria[] = phase.materias ?? []

          return (
            <section className="phase-accordion" key={phase.id}>
              <button
                type="button"
                className={`phase-accordion-trigger ${selectedPhaseId === phase.id ? 'active' : ''}`}
                onClick={() => {
                  onSelectPhase(phase.id)
                  setExpandedPhaseId((current) => current === phase.id ? null : phase.id)
                }}
                aria-expanded={isExpanded}
                aria-controls={`phase-subjects-${phase.id}`}
              >
                <span>{phase.nome}</span>
                <svg aria-hidden="true" viewBox="0 0 20 20" className={isExpanded ? 'expanded' : ''}>
                  <path d="m5 7.5 5 5 5-5" />
                </svg>
              </button>
              {isExpanded && (
                <div className="phase-subject-list" id={`phase-subjects-${phase.id}`}>
                  <button
                    type="button"
                    className={`subject-item ${selectedPhaseId === phase.id && selectedMateriaId === null ? 'active' : ''}`}
                    onClick={() => {
                      onSelectPhase(phase.id)
                      onSelectMateria(null)
                    }}
                  >
                    Todas as matérias
                  </button>
                  {materias.map((materia) => (
                    <button
                      type="button"
                      key={materia.id}
                      className={`subject-item ${selectedMateriaId === materia.id ? 'active' : ''}`}
                      onClick={() => {
                        onSelectPhase(phase.id)
                        onSelectMateria(materia.id)
                      }}
                    >
                      {materia.nome}
                    </button>
                  ))}
                </div>
              )}
            </section>
          )
        })}
      </div>
    </aside>
  )
}
