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

interface ImportMetaEnv {
  // api 请求地址
  readonly VITE_API_BASE_URL: string;
  // 后端服务地址
  readonly VITE_SERVER_URL: string;
  // 第三方登录跳转回调
  readonly VITE_LOGIN_REDIRECT_URI: string;
  // welink
  readonly VITE_WELINK_CLIENT_ID: string;
  // 数据埋点
  readonly VITE_MATOMO_URL: string;
  readonly VITE_MATOMO_SITE_ID: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}


declare module '*.svg?react' {
  import {FC, SVGProps} from 'react';
  const content: FC<SVGProps<SVGSVGElement>>;
  export default content;
}

declare module '*.svg' {
  const content: string;
  export default content;
}
