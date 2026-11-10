import { defineConfig } from '@playwright/test';

const apiBaseUrl = process.env.API_BASE_URL ?? 'http://localhost:8080';

export default defineConfig({
  testDir: './e2e',
  testMatch: '**/*.spec.ts',
  fullyParallel: false,
  retries: 0,
  reporter: 'list',
  use: {
    baseURL: `${apiBaseUrl.replace(/\/+$/, '')}/`,
    extraHTTPHeaders: {
      Accept: 'application/json',
    },
  },
});
