import { Link } from 'react-router-dom'
import logo from '../assets/logo.svg'

export function LandingPage() {
  return (
    <div className="landing-shell">
      <header className="landing-header">
        <div className="brand-block">
          <img src={logo} alt="ProSIGAA logo" className="brand-logo" />
          <span>ProSIGAA</span>
        </div>

        <nav className="landing-nav" aria-label="Menu principal">
          <a href="#como-funciona">Como funciona</a>
          <a href="#recursos">Recursos</a>
        </nav>

        <Link to="/feed" className="primary-button large">Começar grátis</Link>
      </header>

      <main className="landing-main">
        <section className="hero-section" id="como-funciona">
          <div className="hero-copy">
            <div className="eyebrow">ESTUDE JUNTO. EVOLUA MAIS.</div>
            <h1>Diga adeus à bagunça dos grupos de estudo.</h1>
            <p>
              O ProSIGAA organiza dúvidas e materiais por disciplina para você focar no que importa:
              aprender junto.
            </p>
            <div className="cta-group">
              <Link to="/feed" className="primary-button">Acessar plataforma</Link>
              <a href="#recursos" className="ghost-button">Ver como funciona</a>
            </div>
          </div>

          <div className="hero-preview">
            <div className="preview-window">
              <div className="preview-topbar">
                <div className="traffic-dots">
                  <span className="dot red" />
                  <span className="dot yellow" />
                  <span className="dot green" />
                </div>
                <span>ProSIGAA / Feed</span>
              </div>

              <div className="preview-body">
                <div className="preview-nav-row">
                  <div className="mini-pill">MINHAS FASES</div>
                  <button type="button" className="mini-action">+ Nova postagem</button>
                </div>

                <div className="preview-list">
                  <div className="preview-card">
                    <span className="card-kicker">CÁLCULO I • DÚVIDA</span>
                    <h3>Como resolver limites com indeterminação?</h3>
                    <p>Maria L. • 12 curtidas • 4 respostas</p>
                  </div>

                  <div className="preview-card muted">
                    <span className="card-kicker">ALGORITMOS • MATERIAL</span>
                    <h3>Resumo visual: estruturas de dados</h3>
                    <p>João P. • 8 curtidas • 2 respostas</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
      </main>

      <section className="feature-band" id="recursos">
        <div className="feature-item">
          <strong>Feed por disciplina</strong>
          <p>Filtre por fases, matérias e módulos com navegação direta.</p>
        </div>
        <div className="feature-item">
          <strong>Respostas validadas</strong>
          <p>Professores marcam respostas confiáveis com selo exclusivo.</p>
        </div>
        <div className="feature-item">
          <strong>Postagem anônima</strong>
          <p>Compartilhe dúvidas sem expor identidade na turma.</p>
        </div>
      </section>
    </div>
  )
}
