/*
 * Copyright 2024-2025 fuhouyu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'
import {resolve} from 'path';

// https://vitejs.dev/config/
export default defineConfig({
    base: process.env.VITE_BASE_URL,
    plugins: [
        react(),
    ],
    css: {
        preprocessorOptions: {
            scss: {
                api: 'modern-compiler',
            },
        }
    },
    resolve: {
        alias: {
            '@': resolve(__dirname, 'src'),
            '@components': resolve(__dirname, 'src/components'),
            '@assets': resolve(__dirname, 'public'),
        }
    },
    build: {
        assetsDir: 'public'
    },
    server: {
        host: '0.0.0.0',
        proxy: {
            '/api': {
                target: 'http://localhost:8080', // 后端服务地址
                changeOrigin: true, // 是否允许跨域
                rewrite: (path) => path.replace(/^\/api/, '') // 重写路径
            }
        }
    }
})
