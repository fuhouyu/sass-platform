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

import {createBrowserRouter, RouteObject} from "react-router-dom";
import {BaseUrlConstant} from "@/constants/baseUrlConstant.ts";
import {PageLoading} from "@/components";
import {LazyLoad, loader} from "@/router/utils";
import {lazy} from "react";
import Login from "@/views/login";


const routes: RouteObject[] = [
  {
    id: 'layout',
    path: '/',
    element: LazyLoad(lazy(() => import('@/layouts'))),
    loader: loader,
    children: [
      {
        id: 'home',
        path: BaseUrlConstant.HOME_URL,
        element: LazyLoad(lazy(() => import('@/views/home/index.tsx'))),
      },
      {
        id: 'profile',
        path: BaseUrlConstant.USER_PROFILE_URL,
        element: LazyLoad(lazy(() => import('@/views/profile/index.tsx'))),
      }
    ],
    hydrateFallbackElement: <PageLoading/>
  },
  {
    id: 'office-preview',
    path: `${BaseUrlConstant.OFFICE_URL}/:id`,
    element: LazyLoad(lazy(() => import('@/views/office/index.tsx'))),
  },
  {
    id: 'login',
    path: BaseUrlConstant.LOGIN_URL,
    Component: Login
  },
  {
    id: 'redirect',
    path: BaseUrlConstant.REDIRECT_URL,
    element: LazyLoad(lazy(() => import('@/views/redirect/index.tsx'))),
  },
  {
    id: 'account-bind',
    path: '/account-bind',
    element: LazyLoad(lazy(() => import('@/views/profile/account/index.tsx'))),
  },
  {
    id: '404',
    path: '/*',
    element: LazyLoad(lazy(() => import('@/views/error/notfound/index.tsx'))),
    loader: loader,
  }
];
const router = createBrowserRouter(routes);
export default router;
