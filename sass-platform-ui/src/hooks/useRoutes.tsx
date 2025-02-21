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

import {NotFound} from "../pages/error/notfound/NotFound.tsx";
import {lazy, Suspense, useEffect, useState} from "react";
import {PageLoading} from "@/components";
import {Menu} from "@/model/menu.tsx";
import {router, RouterType} from "@/routes/routers.tsx";
import {getAccessToken} from "@/utils";
import {useUserStore} from "@/store";


const modules = import.meta.glob('../pages/**/index.tsx');
const lazyElement = (path: string) => {
    const module = modules[`../pages/${path}/index.tsx`];
    if (!module) {
        return (<NotFound/>);
    }
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    const Component = lazy(module);
    return (
        <Suspense fallback={<PageLoading/>}>
            <Component/>
        </Suspense>
    );
};

export const parseRoutes = (menuProps: Menu[]): RouterType[] => {

    if (menuProps === undefined || menuProps.length === 0) {
        return [];
    }
    return menuProps.map((item) => {
        return {
            id: item.id!,
            title: item.permissionName ?? '',
            path: item.routePath ?? '',
            children: item.children ? parseRoutes(item.children) : [],
            element: item.componentPath && lazyElement(item.componentPath),
        }
    })
};

/**
 * 路由hook
 */
export const useRoutes = () => {
    const {fetchUserMenus} = useUserStore();
    const [initialized, setInitialized] = useState(false);
    useEffect(() => {
        const accessToken = getAccessToken();
        if (!accessToken) {
            setInitialized(true);
            return;
        }
        const initializeRoutes = async () => {
            if (initialized) return; // 避免重复调用

            const userMenus = await fetchUserMenus();
            if (router.routes[0]?.children) {
                router.routes[0].children.push(...parseRoutes(userMenus));
            }
            setInitialized(true);
        };
        initializeRoutes().then();
    }, [fetchUserMenus, initialized]);

    return initialized;
}