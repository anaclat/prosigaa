import { Link } from 'react-router-dom'
import logo from '../../assets/logo.png'
import { useTheme } from '../../hooks/useTheme'

export function Header() {
  const { theme, toggleTheme } = useTheme()

  return (
    <header className="site-header">
      <Link to="/" className="brand-inline" aria-label="Página inicial">
        <img src={logo} alt="Logo ProSIGAA" className="h-10 w-auto brand-logo" />
      </Link>

      <nav className="main-nav" aria-label="Navegação principal">
        <Link to="/feed">Feed</Link>
        <Link to="/feed">Dúvidas</Link>
        <Link to="/feed">Materiais</Link>
      </nav>

      <div className="header-actions">
        <Link to="/login" className="header-login">Entrar</Link>
        <button
          type="button"
          className="theme-switch"
          onClick={toggleTheme}
          aria-label={`Ativar tema ${theme === 'dark' ? 'claro' : 'escuro'}`}
          title={`Ativar tema ${theme === 'dark' ? 'claro' : 'escuro'}`}
        >
          {theme === 'dark' ? (
            <svg aria-hidden="true" viewBox="0 0 24 24">
              <circle cx="12" cy="12" r="4" />
              <path d="M12 2v2m0 16v2M4.93 4.93l1.42 1.42m11.3 11.3 1.42 1.42M2 12h2m16 0h2M4.93 19.07l1.42-1.42m11.3-11.3 1.42-1.42" />
            </svg>
          ) : (
            <svg aria-hidden="true" viewBox="0 0 24 24">
              <path d="M20.9 13A9 9 0 0 1 11 3.1 9 9 0 1 0 20.9 13Z" />
            </svg>
          )}
        </button>
      </div>
    </header>
  )
}
