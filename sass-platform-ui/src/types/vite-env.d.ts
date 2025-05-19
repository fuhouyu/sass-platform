interface ImportMetaEnv {
  // api 请求地址
  readonly VITE_API_URL: string;
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
