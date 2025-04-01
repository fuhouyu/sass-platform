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
import {lazy, useEffect, useState} from "react";
import {Menu} from "@/model/menu.tsx";
import {getAccessToken} from "@/utils";
import {useUserStore} from "@/store";
import {DataRouteObject} from "react-router-dom";


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
            <Component/>
    );
};

export const parseRoutes = (menuProps: Menu[]): DataRouteObject[] => {

    if (menuProps === undefined || menuProps.length === 0) {
        return [];
    }
    return menuProps.map((item) => {
        return {
            id: item.id!,
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
    const {userMenus, fetchUserMenus} = useUserStore();
    const [initialized, setInitialized] = useState(false);
    const [dynamicRoutes, setRoutes] = useState<DataRouteObject[]>([]);

    useEffect(() => {
        const accessToken = getAccessToken();
        if (!accessToken) {
            setInitialized(true);
            return;
        }

        const initializeRoutes = async () => {
            if (initialized || userMenus) return; // Avoid duplicate calls
            const menus = await fetchUserMenus();
            const newRoutes = parseRoutes(menus); // Parse new routes
            setRoutes(newRoutes); // Update routes

            setInitialized(true);
        };

        initializeRoutes().then();
    }, [userMenus, initialized]);

    return {initialized, dynamicRoutes};
}