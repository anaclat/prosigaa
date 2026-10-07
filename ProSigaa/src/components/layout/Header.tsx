import { Link } from 'react-router-dom'
import logo from '../../assets/logo.svg'
import { useTheme } from '../../hooks/useTheme'

export function Header() {
  const { theme, toggleTheme } = useTheme()

  return (
    <header className="site-header">
      <div className="header-brand-wrap">
        <Link to="/" className="brand-inline" aria-label="Ir para a página inicial">
          <img src={logo} alt="Logo ProSIGAA" className="brand-logo" />
          <span>ProSIGAA</span>
        </Link>
      </div>

      <nav className="main-nav" aria-label="Navegação principal">
        <Link to="/feed">Feed</Link>
        <Link to="/feed">Dúvidas</Link>
        <Link to="/feed">Materiais</Link>
      </nav>

      <button type="button" className="theme-switch" onClick={toggleTheme} aria-label="Alternar tema">
        {theme === 'dark' ? '☀️' : '🌙'}
      </button>
    </header>
  )
}
