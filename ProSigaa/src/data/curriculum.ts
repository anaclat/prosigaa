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
    disciplinas: [
      'Pensamento Computacional e Algoritmos',
      'Fundamentos de Arquitetura e Organização de Computadores',
      'Fundamentos da Matemática',
      'Introdução à Computação',
      'Inglês Instrumental I',
      'Lógica Matemática'
    ],
  },
  {
    id: CurriculoPeriodo.SEGUNDO,
    nome: '2º período',
    disciplinas: [
      'Atividades de Extensão I',
      'Comunicação e Expressão',
      'Fundamentos de Probabilidade e Estatística',
      'Programação Frontend I',
      'Programação Orientada a Objetos',
      'Redes de Computadores'
    ],
  },
  {
    id: CurriculoPeriodo.TERCEIRO,
    nome: '3º período',
    disciplinas: [
      'Banco de Dados',
      'Estrutura de Dados',
      'Engenharia de Software I',
      'Programação Frontend II',
      'Segurança da Informação'
    ],
  },
  {
    id: CurriculoPeriodo.QUARTO,
    nome: '4º período',
    disciplinas: [
      'Atividades de Extensão II',
      'Engenharia de Software II',
      'Interface Humano-Computador',
      'Programação Backend',
      'Sistemas Operacionais'
    ],
  },
  {
    id: CurriculoPeriodo.QUINTO,
    nome: '5º período',
    disciplinas: [
      'Inglês Instrumental II',
      'Libras',
      'Tópicos Especiais',
      'Atividades de Extensão III',
      'Introdução à Ciência de Dados e Aprendizado de Máquina',
      'Infraestrutura e Serviços em Nuvem',
      'Programação para Aplicativos Móveis'
    ],
  },
];