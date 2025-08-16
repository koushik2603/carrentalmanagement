import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  // ADD THIS SERVER CONFIGURATION
  server: {
    proxy: {
      // Any request starting with /api will be forwarded to your backend
      '/api': {
        target: 'http://localhost:8082', // Your API Gateway's local address
        changeOrigin: true,
      },
      // Any request starting with /auth will also be forwarded
      '/auth': {
        target: 'http://localhost:8082',
        changeOrigin: true,
      }
    }
  }
})