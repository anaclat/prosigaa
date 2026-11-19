const envApiUrl = (import.meta.env.VITE_API_URL || '').trim()

export const API_URL = (envApiUrl || '/api').replace(/\/+$/, '')

export async function readApiError(response: Response, fallback: string): Promise<string> {
  try {
    const payload = await response.json() as {
      mensagem?: unknown
      message?: unknown
      detail?: unknown
    }

    for (const value of [payload.mensagem, payload.message, payload.detail]) {
      if (typeof value === 'string' && value.trim()) return value.trim()
    }
  } catch {
    // Respostas sem JSON usam a mensagem amigável específica da operação.
  }

  return fallback
}

export function getUserFacingError(error: unknown, fallback: string): string {
  if (error instanceof TypeError) {
    return 'Não foi possível conectar ao servidor. Verifique sua conexão e tente novamente.'
  }
  if (error instanceof Error && error.message.trim()) return error.message
  return fallback
}
