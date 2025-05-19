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

import {ConfigEnv, defineConfig, loadEnv, UserConfig} from 'vite'
import react from '@vitejs/plugin-react'
import {resolve} from 'path';
import checker from 'vite-plugin-checker';

import svgr from "vite-plugin-svgr";

// https://vitejs.dev/config/

export default defineConfig(({mode}: ConfigEnv): UserConfig => {
  // 获取`.env`环境配置文件
  const env = loadEnv(mode, process.cwd());
  const api = env.VITE_API_BASE_URL ?? '/api';
  return {
    base: env.VITE_NODE_ENV === 'development' ? './' : undefined, // 此配置仅为github pages部署用，请自行修改或删除（一般情况下直接移除就行）
    plugins: [
      react(),
      svgr({svgrOptions: {icon: true,}, include: "**/*.svg?react"}),
      // 在浏览器中直接看到上报的类型错误（更严格的类型校验）
      checker({
        typescript: true,
        eslint: {
          useFlatConfig: true,
          lintCommand: 'eslint "./src/**/*.{ts,tsx}"',
        },
      }),
    ],
    resolve: {
      alias: {
        '@': resolve(__dirname, 'src'),
        '@components': resolve(__dirname, 'src/components'),
      },
    },
    css: {
      preprocessorOptions: {
        scss: {
          api: 'modern-compiler',
          additionalData: `@use "@/styles/scss/index.scss" as *;`
        },
      },
    },
    // 反向代理解决跨域问题
    server: {
      // open: true,// 运行时自动打开浏览器
      host: '0.0.0.0', // 局域网别人也可访问
      proxy: {
        [api]: {
          target: env.VITE_SERVER_URL,
          changeOrigin: true, // 是否允许跨域
          rewrite: (path: string) => path.replace(new RegExp('^' + api), ''),
        },
      },
    },
    esbuild:
      env.VITE_NODE_ENV === 'development'
        ? undefined
        : {
          /** 打包时移除 console.log */
          pure: ['console.log'],
          /** 打包时移除 debugger */
          drop: ['debugger'],
        },
    build: {
      target: 'esnext', // 最低 es2015/es6
      outDir: env.VITE_OUT_DIR || 'dist',
      chunkSizeWarningLimit: 2000, // 单个 chunk 文件的大小超过 2000kB 时发出警告（默认：超过500kb警告）
      rollupOptions: {
        // 分包
        output: {
          chunkFileNames: 'assets/js/[name]-[hash].js',
          entryFileNames: 'assets/js/[name]-[hash].js',
          assetFileNames: 'assets/[ext]/[name]-[hash].[ext]',
        },
      },
    },
    // 预构建的依赖项，优化开发（该优化器仅在开发环境中使用）
    optimizeDeps: {
      include: [
        'react',
        'react-dom',
        'react-router',
        'zustand',
        'classnames',
        'es-toolkit',
        'axios',
        'dayjs',
        'immer',
        'ahooks',
      ],
    },
  };
});
