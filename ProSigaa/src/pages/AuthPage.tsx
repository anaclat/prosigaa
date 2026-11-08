import { useState, type FormEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Header } from '../components/layout/Header'
import { API_URL } from '../services/api'
import logo from '../assets/logo.png'

type AuthMode = 'login' | 'register'

export function AuthPage({ initialMode = 'login' }: { initialMode?: AuthMode }) {
  const [mode, setMode] = useState<AuthMode>(initialMode)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [errorMessage, setErrorMessage] = useState('')
  const navigate = useNavigate()

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    const form = new FormData(event.currentTarget)
    const payload = mode === 'login'
      ? { email: String(form.get('email')), senha: String(form.get('password')) }
      : {
        nome: String(form.get('name')),
        email: String(form.get('email')),
        senha: String(form.get('password')),
        tipo: 'ALUNO',
      }

    setIsSubmitting(true)
    setErrorMessage('')
    try {
      const endpoint = mode === 'login' ? '/usuarios/sessao' : '/usuarios'
      const response = await fetch(`${API_URL}${endpoint}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      })
      if (!response.ok) {
        const message = await response.text()
        throw new Error(message || 'Não foi possível autenticar. Verifique seus dados e tente novamente.')
      }

      const user = await response.json() as { id?: number; nome?: string }
      if (!user.id) throw new Error('A resposta do servidor não incluiu o identificador do usuário.')

      window.localStorage.setItem('usuarioId', String(user.id))
      if (user.nome) window.localStorage.setItem('usuarioNome', user.nome)
      navigate('/feed', { replace: true })
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : 'Não foi possível conectar ao servidor.')
    } finally {
      setIsSubmitting(false)
    }
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
            {errorMessage && <p className="form-error" role="alert">{errorMessage}</p>}
            <button className="auth-submit" type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Aguarde...' : mode === 'login' ? 'Entrar' : 'Criar conta'}
            </button>
          </form>

          <Link className="auth-back-link" to="/">Voltar para a página inicial</Link>
        </section>
        <aside className="auth-aside" aria-label="Sobre o ProSigaa">
          <div className="auth-aside-shape auth-aside-shape-one" />
          <div className="auth-aside-shape auth-aside-shape-two" />

          {/* A mágica acontece aqui: d-block e mx-auto centralizam a imagem sozinha */}
          <img src={logo} alt="Logo ProSIGAA" className="h-30 w-40 d-block mx-auto" />

          <p>O conhecimento da sua turma, organizado para continuar ajudando.</p>
          <span className="auth-aside-caption">UM ESPAÇO COLABORATIVO PARA O IFSC</span>
        </aside>

      </main>
    </div>
  )
}