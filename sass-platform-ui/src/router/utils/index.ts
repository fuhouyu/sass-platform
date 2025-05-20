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


import {LoaderFunctionArgs} from "react-router-dom";
import {getAccessToken} from "@/utils";

export * from './lazy-load';

/** 路由白名单 */
export const WHITE_LIST = new Set([
  '/login',
  '/404'
]);

/**
 * 路由守卫
 */
export function loader({request}: LoaderFunctionArgs) {
  const token = getAccessToken();
  const pathname = getPathName(request.url);
  // 未登录且不在白名单中，跳转到登录页
  if (!token && !WHITE_LIST.has(pathname)) {
    window.location.replace(`/login?callback=${encodeURIComponent(window.location.href)}`);
    return false;
  }
  return true;
}

/**
 * 从给定的 URL 中获取 pathname
 */
export function getPathName(url: string): string {
  try {
    const parsedUrl = new URL(url);
    return parsedUrl.pathname;
  } catch {
    return window.location.pathname;
  }
}
