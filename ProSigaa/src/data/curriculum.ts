export enum CurriculoPeriodo {
  PRIMEIRO = 'primeiro',
  SEGUNDO = 'segundo',
  TERCEIRO = 'terceiro',
  QUARTO = 'quarto',
  QUINTO = 'quinto',
}

export type PeriodoCurricular = {
  id: CurriculoPeriodo
  nome: string
  disciplinas: string[]
}

export const curriculumPeriods: PeriodoCurricular[] = [
  {
    id: CurriculoPeriodo.PRIMEIRO,
    nome: '1º período',
    disciplinas: ['Cálculo I', 'Algoritmos', 'Física I', 'Programação I', 'Inglês Instrumental'],
  },
  {
    id: CurriculoPeriodo.SEGUNDO,
    nome: '2º período',
    disciplinas: ['Cálculo II', 'Estruturas de Dados', 'Física II', 'Banco de Dados', 'Álgebra Linear'],
  },
  {
    id: CurriculoPeriodo.TERCEIRO,
    nome: '3º período',
    disciplinas: ['Sistemas Operacionais', 'Lógica Digital', 'Redes de Computadores', 'Análise de Algoritmos', 'Engenharia de Software I'],
  },
  {
    id: CurriculoPeriodo.QUARTO,
    nome: '4º período',
    disciplinas: ['Inteligência Artificial', 'Banco de Dados Avançado', 'Desenvolvimento Web', 'Testes de Software', 'Gestão de Projetos'],
  },
  {
    id: CurriculoPeriodo.QUINTO,
    nome: '5º período',
    disciplinas: ['Arquitetura de Software', 'Segurança da Informação', 'Computação em Nuvem', 'Projeto Integrador', 'Optativa de Formação'],
  },
]
