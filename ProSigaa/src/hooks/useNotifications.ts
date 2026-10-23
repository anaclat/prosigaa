import { useCallback, useEffect, useState } from 'react'
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { API_URL } from '../services/api'

export interface AppNotification {
  id: string
  message: string
  type: string
  createdAt: string
}

type ConnectionStatus = 'connecting' | 'connected' | 'disconnected' | 'unconfigured' | 'error'

function getCurrentUserId(): string | null {
  const sessionUserId = window.localStorage.getItem('usuarioId')
  if (sessionUserId) return sessionUserId
  const configuredId = import.meta.env.VITE_USER_ID?.trim()
  if (configuredId) return configuredId
  return window.localStorage.getItem('prosigaa-user-id')
}

function parseNotification(payload: unknown): AppNotification | null {
  if (typeof payload !== 'object' || payload === null) return null

  const data = payload as Record<string, unknown>
  if (typeof data.mensagem !== 'string' || !data.mensagem.trim()) return null

  const id = typeof data.id === 'string' || typeof data.id === 'number'
    ? String(data.id)
    : `${Date.now()}`
  const type = typeof data.tipoNotificacao === 'string'
    ? data.tipoNotificacao
    : typeof data.tipo === 'string' ? data.tipo : 'AVISO'
  const createdAt = typeof data.criadoEm === 'string'
    ? data.criadoEm
    : typeof data.timestamp === 'string' ? data.timestamp : new Date().toISOString()

  return { id, message: data.mensagem, type, createdAt }
}

export function useNotifications() {
  const [userId] = useState(getCurrentUserId)
  const [notifications, setNotifications] = useState<AppNotification[]>([])
  const [latestNotification, setLatestNotification] = useState<AppNotification | null>(null)
  const [unreadCount, setUnreadCount] = useState(0)
  const [status, setStatus] = useState<ConnectionStatus>(() => userId ? 'connecting' : 'unconfigured')
  const [connectionError, setConnectionError] = useState('')
  const dismissToast = useCallback(() => setLatestNotification(null), [])
  const markAllAsRead = useCallback(() => setUnreadCount(0), [])

  useEffect(() => {
    if (!userId) return
    if (!/^\d+$/.test(userId)) {
      setStatus('error')
      setConnectionError('O ID do usuário para notificações deve ser numérico.')
      return
    }

    const client = new Client({
      webSocketFactory: () => new SockJS(`${API_URL}/ws`),
      reconnectDelay: 5000,
      onConnect: () => {
        setStatus('connected')
        setConnectionError('')
        client.subscribe(`/topic/usuarios/${userId}/notifications`, (frame) => {
          try {
            const notification = parseNotification(JSON.parse(frame.body) as unknown)
            if (!notification) {
              setConnectionError('O servidor enviou uma notificação em formato inválido.')
              console.error('Notificação WebSocket inválida:', frame.body)
              return
            }

            setNotifications((current) => [notification, ...current].slice(0, 30))
            setLatestNotification(notification)
            setUnreadCount((count) => count + 1)
          } catch (error) {
            setConnectionError('Não foi possível interpretar uma notificação recebida.')
            console.error('Falha ao interpretar notificação WebSocket:', error)
          }
        })
      },
      onStompError: (frame) => {
        const message = frame.headers.message || 'Falha na conexão de notificações.'
        setStatus('error')
        setConnectionError(message)
        console.error('Erro STOMP:', message, frame.body)
      },
      onWebSocketError: (event) => {
        setStatus('error')
        setConnectionError('Não foi possível conectar ao servidor de notificações.')
        console.error('Erro no WebSocket de notificações:', event)
      },
      onWebSocketClose: () => {
        setStatus('disconnected')
      },
    })

    try {
      client.activate()
    } catch (error) {
      setStatus('error')
      setConnectionError('Não foi possível iniciar a conexão de notificações.')
      console.error('Falha ao iniciar o cliente STOMP:', error)
    }

    return () => {
      void client.deactivate()
    }
  }, [userId])

  return {
    notifications,
    latestNotification,
    unreadCount,
    status,
    connectionError,
    dismissToast,
    markAllAsRead,
  }
}
