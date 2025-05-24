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

import {lazy, useCallback, useState} from "react";
import {IMenu} from "@/types/menu";
import {getAccessToken} from "@/utils";
import {useRouterStore, useUserStore} from "@/store";
import {createBrowserRouter, DataRouteObject} from "react-router-dom";
import NotFound from "@/views/error/notfound";
import router from "@/router";
import {loader} from "@/router/utils";


const modules = import.meta.glob('../views/**/index.tsx');
const lazyElement = (path: string) => {
  const module = modules[`../views/${path}/index.tsx`];
  if (!module) {
    return (<NotFound/>);
  }
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-expect-error
  const Component = lazy(module);
  return (
    <Component/>
  );
};

const parseRoutes = (menuProps: IMenu[]): DataRouteObject[] => {

  if (menuProps === undefined || menuProps.length === 0) {
    return [];
  }
  return menuProps.map((item) => {
    return {
      id: item.id!,
      path: item.routePath ?? '',
      children: item.children ? parseRoutes(item.children) : [],
      element: item.componentPath && lazyElement(item.componentPath),
      loader: loader,
    }
  })
};

/**
 * 路由hook
 */
export const useRoutes = () => {
  const {userMenus, fetchUserMenus} = useUserStore();
  const [initialized, setInitialized] = useState(false);
  const {storeRouter} = useRouterStore(state => state);

  /**
   * 更新动态路由
   */
  const updateDynamicRoutes = useCallback(async () => {
    const rootRoutes = [...router.routes];
    const accessToken = getAccessToken();
    if (!accessToken) {
      const updatedRouter = createBrowserRouter(rootRoutes);
      storeRouter(updatedRouter);
      setInitialized(true);
      return;
    }
    if (initialized || userMenus) return;
    const menus = await fetchUserMenus();
    setInitialized(true);
    const dynamicRoutes = parseRoutes(menus);
    rootRoutes[0].children = [...dynamicRoutes, ...(rootRoutes[0].children ?? [])];
    const updatedRouter = createBrowserRouter(rootRoutes);
    storeRouter(updatedRouter);
  }, [userMenus, initialized, fetchUserMenus, storeRouter]);

  return {initialized, updateDynamicRoutes};
}
