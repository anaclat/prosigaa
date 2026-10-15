import { Routes, Route } from 'react-router-dom'
import { AppProvider } from './context/AppContext'
import { LandingPage } from './pages/LandingPage'
import { FeedPage } from './pages/FeedPage'
import { CommentsPage } from './pages/CommentsPage'
import { AuthPage } from './pages/AuthPage'

function App() {
  return (
    <AppProvider>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/login" element={<AuthPage />} />
        <Route path="/feed" element={<FeedPage />} />
        <Route path="/comentarios/:postId" element={<CommentsPage />} />
      </Routes>
    </AppProvider>
  )
}

export default App
