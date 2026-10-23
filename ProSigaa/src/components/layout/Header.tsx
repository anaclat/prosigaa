import { useEffect, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import logo from '../../assets/logo.png'
import { useTheme } from '../../hooks/useTheme'
import { useNotifications } from '../../hooks/useNotifications'

export function Header() {
  const { theme, toggleTheme } = useTheme()
  const navigate = useNavigate()
  const userName = window.localStorage.getItem('usuarioNome')
  const [notificationsOpen, setNotificationsOpen] = useState(false)
  const {
    notifications,
    latestNotification,
    unreadCount,
    status,
    connectionError,
    dismissToast,
    markAllAsRead,
  } = useNotifications()

  useEffect(() => {
    if (!latestNotification) return
    const timer = window.setTimeout(dismissToast, 5000)
    return () => window.clearTimeout(timer)
  }, [dismissToast, latestNotification])

  const toggleNotifications = () => {
    setNotificationsOpen((open) => !open)
    markAllAsRead()
  }

  const handleLogout = () => {
    window.localStorage.removeItem('usuarioId')
    window.localStorage.removeItem('usuarioNome')
    window.localStorage.removeItem('prosigaa-user-id')
    navigate('/', { replace: true })
  }

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
        {userName ? (
          <>
            <span className="header-user-name">{userName}</span>
            <button type="button" className="header-login" onClick={handleLogout}>Sair</button>
          </>
        ) : (
          <Link to="/login" className="header-login">Entrar</Link>
        )}
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
        <div className="notification-menu">
          <button
            type="button"
            className="notification-toggle"
            onClick={toggleNotifications}
            aria-label={unreadCount ? `Notificações, ${unreadCount} não lidas` : 'Notificações'}
            aria-expanded={notificationsOpen}
            aria-controls="notification-dropdown"
          >
            <svg aria-hidden="true" viewBox="0 0 24 24">
              <path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9M10 21h4" />
            </svg>
            {unreadCount > 0 && (
              <span className="notification-count">{unreadCount > 99 ? '99+' : unreadCount}</span>
            )}
          </button>
          {notificationsOpen && (
            <section className="notification-dropdown" id="notification-dropdown" aria-label="Histórico de notificações">
              <div className="notification-dropdown-header">
                <h2>Notificações</h2>
                <span className={`notification-connection ${status}`} role="status">
                  {status === 'connected' ? 'Conectado' : status === 'connecting' ? 'Conectando...' : status === 'unconfigured' ? 'Sem usuário configurado' : status === 'error' ? 'Erro de conexão' : 'Desconectado'}
                </span>
              </div>
              {connectionError && <p className="notification-connection-error" role="alert">{connectionError}</p>}
              {notifications.length === 0 ? (
                <p className="notification-empty">
                  {status === 'unconfigured'
                    ? 'Configure VITE_USER_ID ou a chave prosigaa-user-id para receber avisos.'
                    : 'Você ainda não recebeu notificações.'}
                </p>
              ) : (
                <ul className="notification-list">
                  {notifications.map((notification) => (
                    <li key={notification.id}>
                      <span className="notification-type">{notification.type}</span>
                      <p>{notification.message}</p>
                      <time dateTime={notification.createdAt}>{new Date(notification.createdAt).toLocaleString('pt-BR')}</time>
                    </li>
                  ))}
                </ul>
              )}
            </section>
          )}
        </div>
      </div>
      {latestNotification && (
        <div className="notification-toast" role="status" aria-live="polite">
          <span aria-hidden="true">🔔</span>
          <p>{latestNotification.message}</p>
          <button type="button" onClick={dismissToast} aria-label="Fechar notificação">×</button>
        </div>
      )}
    </header>
  )
}
