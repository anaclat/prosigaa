import { useState, type ReactNode } from 'react'
import { Link } from 'react-router-dom'
import logo from '../assets/logo.png'
import { useTheme } from '../hooks/useTheme'

type IconName = 'sun' | 'moon' | 'menu' | 'close' | 'arrow' | 'search' | 'badge' | 'eye' | 'tabs'

function Icon({ name, size = 20 }: { name: IconName; size?: number }) {
  const paths: Record<IconName, ReactNode> = {
    sun: (
      <>
        <circle cx="12" cy="12" r="4" />
        <path d="M12 2v2m0 16v2M4.93 4.93l1.42 1.42m11.3 11.3 1.42 1.42M2 12h2m16 0h2M4.93 19.07l1.42-1.42m11.3-11.3 1.42-1.42" />
      </>
    ),
    moon: <path d="M20.9 13A9 9 0 0 1 11 3.1 9 9 0 1 0 20.9 13Z" />,
    menu: <path d="M4 7h16M4 12h16M4 17h16" />,
    close: <path d="m18 6-12 12M6 6l12 12" />,
    arrow: <path d="M5 12h14m-7-7 7 7-7 7" />,
    search: (
      <>
        <circle cx="11" cy="11" r="7" />
        <path d="m20 20-4-4" />
      </>
    ),
    badge: (
      <>
        <path d="M12 3 14.8 5l3.4.2.8 3.3 2 2.5-1.5 3.1.3 3.4-3.1 1.4-2 2.7-3.3-.9-3.3.9-2-2.7L3 17.5l.3-3.4L1.8 11l2-2.5.8-3.3L8 5l4-2Z" />
        <path d="m8.5 12 2.2 2.2 4.8-4.8" />
      </>
    ),
    eye: (
      <>
        <path d="M2 12s3.6-7 10-7 10 7 10 7-3.6 7-10 7-10-7-10-7Z" />
        <circle cx="12" cy="12" r="3" />
      </>
    ),
    tabs: (
      <>
        <rect x="3" y="5" width="18" height="15" rx="2" />
        <path d="M3 10h18M8 5v5m5-5v5" />
      </>
    ),
  }

  return (
    <svg
      aria-hidden="true"
      width={size}
      height={size}
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="1.8"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      {paths[name]}
    </svg>
  )
}

const features: { icon: IconName; title: string; description: string }[] = [
  {
    icon: 'search',
    title: 'Organização por disciplinas',
    description:
      'Encontre dúvidas e conversas organizadas por fase e matéria, sem perder tempo procurando em grupos.',
  },
  {
    icon: 'badge',
    title: 'Compartilhamento de materiais',
    description:
      'Compartilhe resumos, links e materiais de estudo com quem está aprendendo os mesmos conteúdos.',
  },
  {
    icon: 'eye',
    title: 'Rede colaborativa',
    description:
      'Faça perguntas, troque experiências e construa um espaço de aprendizagem com a sua turma.',
  },
]

export function LandingPage() {
  const { theme, toggleTheme } = useTheme()
  const [menuOpen, setMenuOpen] = useState(false)

  const closeMenu = () => setMenuOpen(false)

  return (
    <div className="lp-page">
      <header className="lp-header">
        <Link to="/" className="lp-brand" aria-label="ProSigaa, início" onClick={closeMenu}>
          <img src={logo} alt="" />
          <span>ProSigaa</span>
        </Link>

        <nav
          className={`lp-nav${menuOpen ? ' is-open' : ''}`}
          id="lp-navigation"
          aria-label="Navegação principal"
        >
          <a href="#solucao" onClick={closeMenu}>Como funciona</a>
          <a href="#recursos" onClick={closeMenu}>Recursos</a>
          <a href="#sobre" onClick={closeMenu}>Sobre o projeto</a>
        </nav>

        <div className="lp-header-actions">
          <button
            className="lp-theme-toggle"
            type="button"
            onClick={toggleTheme}
            aria-label={`Ativar tema ${theme === 'dark' ? 'claro' : 'escuro'}`}
            title={`Ativar tema ${theme === 'dark' ? 'claro' : 'escuro'}`}
          >
            <Icon name={theme === 'dark' ? 'sun' : 'moon'} />
          </button>
          <Link to="/login" className="lp-login-button">Entrar</Link>
          <button
            className="lp-menu-toggle"
            type="button"
            onClick={() => setMenuOpen((open) => !open)}
            aria-label={menuOpen ? 'Fechar menu' : 'Abrir menu'}
            aria-expanded={menuOpen}
            aria-controls="lp-navigation"
          >
            <Icon name={menuOpen ? 'close' : 'menu'} size={22} />
          </button>
        </div>
      </header>

      <main>
        <section className="lp-hero" aria-labelledby="lp-hero-title">
          <div className="lp-hero-copy">
            <span className="lp-eyebrow">
              <span className="lp-eyebrow-dot" />
              FEITO PARA QUEM ESTUDA NO IFSC
            </span>
            <h1 id="lp-hero-title">
              Diga adeus à bagunça dos <span>grupos de estudo.</span>
            </h1>
            <p>
              Organize dúvidas, materiais e conversas por disciplina em um só lugar. Estude em comunidade
              e encontre de novo o que antes se perdia no meio das mensagens.
            </p>
            <div className="lp-hero-actions">
              <Link to="/cadastro" className="lp-primary-button">
                Criar conta <Icon name="arrow" size={18} />
              </Link>
              <Link className="lp-text-link lp-secondary-cta" to="/login">Fazer Login</Link>
            </div>
            <div className="lp-trust-note">
              <span className="lp-trust-mark" aria-hidden="true">✓</span>
              Gratuito para estudantes do IFSC
            </div>
          </div>

          <div className="lp-hero-art" aria-hidden="true">
            <div className="lp-art-orbit lp-art-orbit-one" />
            <div className="lp-art-orbit lp-art-orbit-two" />
            <div className="lp-art-shape lp-art-shape-main">
              <span className="lp-art-monogram">P</span>
            </div>
            <div className="lp-art-shape lp-art-shape-small" />
            <div className="lp-art-spark lp-art-spark-one">✳</div>
            <div className="lp-art-spark lp-art-spark-two">✳</div>
            <div className="lp-art-caption">
              <span className="lp-art-caption-dot" />
              Aprender em comunidade
            </div>
            <div className="lp-art-index">IFSC <span>·</span> 01—04</div>
          </div>
        </section>

        <section className="lp-problem-section" id="solucao" aria-labelledby="lp-problem-title">
          <div className="lp-section-kicker">MENOS RUÍDO, MAIS APRENDIZADO</div>
          <div className="lp-problem-grid">
            <div>
              <h2 id="lp-problem-title">Uma boa resposta merece ser encontrada de novo.</h2>
            </div>
            <div className="lp-problem-copy">
              <p>
                Nos grupos de WhatsApp e Telegram, dúvidas importantes desaparecem entre novas
                mensagens. No ProSigaa, cada contribuição fica salva e organizada por fase e matéria.
              </p>
              <p className="lp-solution-line">
                <span aria-hidden="true">↗</span>
                O conhecimento da turma continua ajudando, semestre após semestre.
              </p>
            </div>
          </div>
        </section>

        <section className="lp-features-section" id="recursos" aria-labelledby="lp-features-title">
          <div className="lp-features-heading">
            <div>
              <div className="lp-section-kicker">APRENDIZADO COM MAIS CONTEXTO</div>
              <h2 id="lp-features-title">Tudo no lugar certo.</h2>
            </div>
            <p>Recursos pensados para deixar a colaboração acadêmica mais simples e útil.</p>
          </div>
          <div className="lp-feature-grid">
            {features.map((feature, index) => (
              <article className="lp-feature-card" key={feature.title}>
                <div className="lp-feature-card-top">
                  <span className="lp-feature-icon"><Icon name={feature.icon} size={22} /></span>
                  <span className="lp-feature-number">0{index + 1}</span>
                </div>
                <h3>{feature.title}</h3>
                <p>{feature.description}</p>
              </article>
            ))}
          </div>
        </section>
      </main>

      <footer className="lp-footer" id="sobre">
        <Link to="/" className="lp-brand lp-footer-brand" aria-label="ProSigaa, início">
          <img src={logo} alt="" />
          <span>ProSigaa</span>
        </Link>
        <p>Projeto acadêmico desenvolvido no IFSC.</p>
        <span className="lp-copyright">© {new Date().getFullYear()} ProSigaa</span>
      </footer>
    </div>
  )
}
