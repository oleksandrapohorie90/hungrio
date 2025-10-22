import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})

// browser talks to Vite on 5173, and Vite proxies those /api calls to 8080 behind the scenes. 
// DevTools shows the URL you requested (5173), not the final hop.
// using Vite, not CRA — and in Vite, the proxy setting belongs in vite.config.ts, not package.json.

/* below Vite is equivalent to - "proxy": "http://localhost:8080" in CRA(Create React App).
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true,
    secure: false,
  },
},
*/