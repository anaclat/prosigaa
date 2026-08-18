# Prosigaa
Trabalho referente à disciplina de backend.

Visão Geral do MVP
Uma plataforma web assíncrona de fórum e repositório acadêmico para o IFSC. O objetivo é permitir que alunos publiquem materiais e tirem dúvidas através de postagens comentadas, organizadas por filtros estruturais da faculdade.

 Escopo de Funcionalidades (O que o sistema faz)
1. Cadastro e Autenticação

Login simples (apenas validação com email @ifsc.edu.br ou @aluno.ifsc.edu.br).

2. Fluxo de Postagem (Feed Central)

O usuário pode criar uma nova postagem.
Obrigatório selecionar: Fase (ex: 1ª Fase, 2ª Fase) e Matéria (ex: Cálculo 1, Programação I).
Obrigatório selecionar a categoria: Dúvida ou Material de Estudo.

3. Filtro e Busca (A funcionalidade principal)

Tela inicial com um feed de postagens recentes.
Barra de filtros no topo com dois campos de seleção (dropdown): Filtrar por Fase e Filtrar por Matéria.

4. Interação

Seção inferior em cada postagem para inserção de comentários em formato de texto simples.
Destaque visual simples se o autor do comentário for um Professor.
 Tecnologias Recomendadas para Agilidade
Frontend: React ou HTML/CSS simples com Tailwind.
Backend: Java (Spring Boot).
Banco de Dados: H2 (No MVP).

Diagrama UML para discussão com os colegas em sala.
<img width="659" height="952" alt="backend drawio" src="https://github.com/user-attachments/assets/515cf293-6d3b-461d-9256-0198380025e7" />

