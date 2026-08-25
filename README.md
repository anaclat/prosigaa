#  ProSigaa — Repositório e Fórum Acadêmico IFSC

> **Plataforma web assíncrona de estudo colaborativo e 100% gratuita para os estudantes do IFSC.**

---

##  Visão Geral do Projeto
O **ProSigaa** é uma aplicação desenvolvida para a disciplina de **Programação Backend e Interfase Humano-Computador**. Seu objetivo é centralizar e organizar a troca de conhecimento entre os alunos do Instituto Federal de Santa Catarina (IFSC).

Através de postagens comentadas e categorizadas por **Fase** e **Matéria**, os estudantes podem publicar materiais de apoio e tirar dúvidas sem que o conteúdo se perda no histórico de mensagens instantâneas ou fique bloqueado por assinaturas pagas.

---

##  O Problema e a Solução

* **O Problema:** Dúvidas e resoluções enviadas em grupos de mensagens (ex: WhatsApp/Telegram) desaparecem rapidamente no histórico. Além disso, plataformas de estudo comerciais (como o Brainly) cobram mensalidades e bloqueiam respostas.
* **A Solução:** Uma plataforma assíncrona, pública e interna do IFSC onde o conhecimento gerado por uma turma fica **salvo e indexado por disciplina**, servindo como um repositório perpétuo para os semestres futuros.

---

##  Público-Alvo
* **Alunos do IFSC:** Usuários que buscam materiais de estudo, criam postagens de dúvidas e interagem via comentários.
* **Professores e Monitores:** Educadores e tutores que validam conteúdos e tiram dúvidas, recebendo **destaque visual exclusivo** em suas respostas.

---

##  Requisitos do Sistema

###  Requisitos Funcionais (RF)
* **`[RF01]` Autenticação Institucional:** Validação de acesso restrita a e-mails com domínio `@ifsc.edu.br` ou `@aluno.ifsc.edu.br`.
* **`[RF02]` Publicação de Conteúdo:** Criação de postagens categorizadas como *Dúvida* ou *Material de Estudo*.
* **`[RF03]` Vinculação Acadêmica:** Obrigatoriedade de associar toda postagem a uma **Fase** (ex: 1ª Fase) e a uma **Matéria** (ex: Programação I).
* **`[RF04]` Filtro Centralizado:** Feed inicial dinâmico com campos de seleção (dropdowns) para filtrar postagens por Fase e Matéria.
* **`[RF05]` Sistema de Comentários:** Seção inferior em cada postagem para inserção de comentários em texto simples.
* **`[RF06]` Destaque de Autoridade:** Identificação e destaque visual automático em comentários feitos por Professores.

###  Requisitos Não-Funcionais (RNF)
* **`[RNF01]` Performance Backend:** Rotas e filtros otimizados para rápida resposta em Java Spring Boot.
* **`[RNF02]` Usabilidade:** Interface fluida, responsiva e focada na facilidade de navegação do estudante.
* **`[RNF03]` Persistência Leve (MVP):** Armazenamento em memória com H2 Database para facilitar a execução local e testes rápidos.

---

##  Modelagem do Sistema (Diagrama UML)

Para orientar o desenvolvimento da camada de modelo do backend, elaboramos o Diagrama de Classes a seguir:

![Diagrama de Classes ProSigaa](https://github.com/user-attachments/assets/515cf293-6d3b-461d-9256-0198380025e7)

---

##  Casos de Uso (Cenários Principais)

###  UC01: Criar uma Postagem de Dúvida
1. O aluno faz login com seu e-mail institucional.
2. Seleciona a **Fase** (ex: *2ª Fase*) e a **Matéria** (ex: *Estrutura de Dados*).
3. Seleciona a categoria **Dúvida**, insere o título e o conteúdo em texto.
4. Clica em **Publicar**. O backend valida os dados e exibe a postagem no feed central.

###  UC02: Filtrar Postagens por Disciplina
1. Na barra superior do feed, o usuário seleciona **Fase: 1ª Fase**.
2. No dropdown de matérias, seleciona **Cálculo 1**.
3. O feed recarrega exibindo exclusivamente as dúvidas e materiais dessa combinação.

---

##  Mapeamento de Interface x Requisitos

| Tela / Componente | Requisitos Atendidos | Descrição |
| :--- | :--- | :--- |
| **Login / Cadastro** | `[RF01]` | Formulário de validação com instrução exclusiva para e-mails do IFSC. |
| **Feed Inicial (Topo)** | `[RF03]`, `[RF04]` | Barra fixa com os dropdowns de seleção (*Filtrar por Fase* e *Filtrar por Matéria*). |
| **Cards do Feed** | `[RF02]` | Lista de postagens exibindo título, conteúdo, autor e a tag (*Dúvida* ou *Material*). |
| **Sessão de Comentários** | `[RF05]`, `[RF06]` | Área inferior da postagem com campo de resposta e badge/cor diferenciada para Professores. |

---

##  Documentação Interativa da API (Swagger)

A API do **ProSigaa** conta com documentação interativa gerada automaticamente via **Springdoc OpenAPI (Swagger UI)**. Através dela, é possível visualizar todas as rotas ativas, analisar a estrutura dos DTOs e testar requisições em tempo real.

###  Como Acessar

1. Certifique-se de que a aplicação backend está rodando localmente com o comando:
   ```bash
   ./gradlew bootRun
Abra o seu navegador e acesse a URL:
 http://localhost:8080/swagger-ui/index.html

##  Tecnologias Utilizadas

* **Backend:** Java (Spring Boot)
* **Banco de Dados:** H2 Database (Banco em memória para o MVP)
* **Frontend:** HTML / CSS / Tailwind ou React
* **Modelagem:** Draw.io
