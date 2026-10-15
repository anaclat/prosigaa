import { createContext, useContext, useEffect, useMemo, useState } from 'react'
import type { ThemeContextValue, ThemeMode } from '../interfaces/forum'

const AppContext = createContext<ThemeContextValue | undefined>(undefined)

export function AppProvider({ children }: { children: React.ReactNode }) {
  const [theme, setTheme] = useState<ThemeMode>(() => {
    const storageTheme = window.localStorage.getItem('prosigaa-theme') as ThemeMode | null

    if (storageTheme === 'light' || storageTheme === 'dark') {
      return storageTheme
    }

    return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
  })

  useEffect(() => {
    document.body.dataset.theme = theme
    document.documentElement.classList.toggle('dark', theme === 'dark')
    window.localStorage.setItem('prosigaa-theme', theme)
  }, [theme])

  const value = useMemo(
    () => ({
      theme,
      toggleTheme: () => setTheme((current) => (current === 'light' ? 'dark' : 'light')),
    }),
    [theme],
  )

  return <AppContext.Provider value={value}>{children}</AppContext.Provider>
}

export function useAppTheme() {
  const context = useContext(AppContext)

  if (!context) {
    throw new Error('useAppTheme must be used within AppProvider')
  }

  return context
}
