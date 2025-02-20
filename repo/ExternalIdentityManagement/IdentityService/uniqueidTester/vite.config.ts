import path from 'node:path'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig(
  { plugins: [vue()],
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src')
      }
    }
  } 
)
/*
mport { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default ({mode}) => {
  process.env = Object.assign(process.env, loadEnv(mode, process.cwd(), ''));
  return defineConfig({plugins: [vue()]});

  , oidc: {
      authority:    import.meta.env.VITE_OIDC_ISSUER
    , clientId:     import.meta.env.VITE_OIDC_CLIENT
    , redirectUri:  'http://localhost:1337/oidc-callback'
    , responseType: 'id_token token'
    , scope:        'openid profile'
    }
}
*/