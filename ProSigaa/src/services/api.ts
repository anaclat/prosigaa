const envApiUrl = (import.meta.env.VITE_API_URL || '').trim()

function getDefaultApiUrl(): string {
	const isBrowser = typeof window !== 'undefined'
	if (!isBrowser) return 'http://localhost:8080'

	const localHosts = new Set(['localhost', '127.0.0.1'])
	if (localHosts.has(window.location.hostname)) {
		return 'http://localhost:8080'
	}

	return '/api'
}

export const API_URL = (envApiUrl || getDefaultApiUrl()).replace(/\/+$/, '')
