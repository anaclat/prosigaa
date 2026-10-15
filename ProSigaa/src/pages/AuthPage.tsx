import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Header } from '../components/layout/Header'

type AuthMode = 'login' | 'register'

export function AuthPage() {
  const [mode, setMode] = useState<AuthMode>('login')

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
  }

  return (
    <div className="feed-page-shell auth-page">
      <Header />
      <main className="auth-layout">
        <section className="auth-card" aria-labelledby="auth-title">
          <div className="auth-intro">
            <span className="auth-eyebrow">COMUNIDADE IFSC</span>
            <h1 id="auth-title">{mode === 'login' ? 'Que bom ter você de volta.' : 'Vamos estudar juntos?'}</h1>
            <p>
              {mode === 'login'
                ? 'Entre para continuar aprendendo com a sua comunidade.'
                : 'Crie sua conta acadêmica e faça parte da comunidade ProSigaa.'}
            </p>
          </div>

          <div className="auth-mode-switch" role="tablist" aria-label="Tipo de acesso">
            <button
              type="button"
              role="tab"
              aria-selected={mode === 'login'}
              className={mode === 'login' ? 'active' : ''}
              onClick={() => setMode('login')}
            >
              Entrar
            </button>
            <button
              type="button"
              role="tab"
              aria-selected={mode === 'register'}
              className={mode === 'register' ? 'active' : ''}
              onClick={() => setMode('register')}
            >
              Criar conta
            </button>
          </div>

          <form className="auth-form" onSubmit={handleSubmit}>
            {mode === 'register' && (
              <label htmlFor="auth-name">
                Nome
                <input id="auth-name" name="name" type="text" autoComplete="name" placeholder="Seu nome" required />
              </label>
            )}
            <label htmlFor="auth-email">
              E-mail institucional
              <input
                id="auth-email"
                name="email"
                type="email"
                autoComplete="email"
                placeholder="nome@ifsc.edu.br"
                required
              />
              <span className="auth-field-hint">Use seu e-mail acadêmico @ifsc.edu.br</span>
            </label>
            <label htmlFor="auth-password">
              Senha
              <input
                id="auth-password"
                name="password"
                type="password"
                autoComplete={mode === 'login' ? 'current-password' : 'new-password'}
                placeholder="Sua senha"
                required
              />
            </label>
            <button className="auth-submit" type="submit">
              {mode === 'login' ? 'Entrar' : 'Criar conta'}
            </button>
            <p className="auth-demo-note">Demonstração acadêmica — autenticação não habilitada.</p>
          </form>

          <Link className="auth-back-link" to="/feed">Explorar o feed sem entrar</Link>
        </section>
        <aside className="auth-aside" aria-label="Sobre o ProSigaa">
          <div className="auth-aside-shape auth-aside-shape-one" />
          <div className="auth-aside-shape auth-aside-shape-two" />
          <span className="auth-aside-symbol" aria-hidden="true">+</span>
          <p>O conhecimento da sua turma, organizado para continuar ajudando.</p>
          <span className="auth-aside-caption">UM ESPAÇO COLABORATIVO PARA O IFSC</span>
        </aside>
      </main>
    </div>
  )
}
