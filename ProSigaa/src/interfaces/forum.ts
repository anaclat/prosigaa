export type ThemeMode = 'light' | 'dark'

export type PostType = 'DUVIDA' | 'MATERIAL_ESTUDO'

export interface Comment {
  id: string
  author: string
  message: string
  time: string
  isProfessor?: boolean
  accent?: 'primary' | 'neutral'
}

export interface ForumMateria {
  id: number
  nome: string
  codigo: string
}

export interface ForumPhase {
  id: number
  nome: string
  numero: number
  materias: ForumMateria[]
}

export interface ForumPost {
  id: string
  title: string
  category: PostType
  description: string
  author: string
  time: string
  categoryLabel: string
  commentsCount: number
  comments: Comment[]
  phase: string
  phaseId?: number
  subject: string
  subjectId?: number
}

export interface ThemeContextValue {
  theme: ThemeMode
  toggleTheme: () => void
}

export interface BackendPostDTO {
  id: number
  titulo: string
  conteudo: string
  urlImagem: string | null
  tipo: 'DUVIDA' | 'MATERIAL_ESTUDO'
  criadoEm: string
  usuario: {
    id: number
    nome: string
    tipo: string
  }
  fase: {
    id: number
    nome: string
    numero: number
  }
  materia: {
    id: number
    nome: string
    codigo: string
  }
  comentarios: Array<{
    id: number
    conteudo: string
    criadoEm: string
    usuario: {
      id: number
      nome: string
      tipo: string
    }
  }>
  curtidas: number
}
