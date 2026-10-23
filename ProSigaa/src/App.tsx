import type { ReactNode } from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import { AppProvider } from './context/AppContext'
import { LandingPage } from './pages/LandingPage'
import { FeedPage } from './pages/FeedPage'
import { CommentsPage } from './pages/CommentsPage'
import { AuthPage } from './pages/AuthPage'

function hasSession(): boolean {
  const userId = Number(window.localStorage.getItem('usuarioId'))
  return Number.isSafeInteger(userId) && userId > 0
}

function RequireAuth({ children }: { children: ReactNode }) {
  return hasSession()
    ? children
    : <Navigate to="/" replace />
}

function App() {
  return (
    <AppProvider>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={hasSession() ? <Navigate to="/feed" replace /> : <AuthPage />} />
        <Route path="/feed" element={<RequireAuth><FeedPage /></RequireAuth>} />
        <Route path="/comentarios/:postId" element={<RequireAuth><CommentsPage /></RequireAuth>} />
        <Route path="/cadastro" element={hasSession() ? <Navigate to="/feed" replace /> : <AuthPage initialMode="register" />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AppProvider>
  )
}

export default App
