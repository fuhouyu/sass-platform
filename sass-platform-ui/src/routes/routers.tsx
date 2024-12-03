/*
 * Copyright 2024-2024 the original author or authors.
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

import {createBrowserRouter} from "react-router-dom";
import type {Router} from "@remix-run/router/dist/router";
import React, {lazy, Suspense} from "react";
import {Layout} from "@/layouts/layout";
import {NotFound} from "@/pages/error/notfound/NotFound";
import {Menu} from "@/model/menu";
import {PageLoading} from "@components/PageLoading/pageLoading";
import {Home} from "@/pages/home/Home";
import {PersonCenter} from "@/pages/userinfo/PersonCenter";
import Login from "@/pages/login";
import {BASE_LOGIN_URL} from "@/constants/commonConstant";

export type RouterType = {
    id: string;
    title: string,
    path: string,
    element?: React.ReactNode | null | undefined,
    component?: React.ReactNode,
    children?: RouterType[]
}


/**
 * 公共路由
 */
export const commonRouter: RouterType[] = [

    {
        id: 'layout',
        title: 'home',
        path: '/',
        element: <Layout/>,
        children: [
            {
                id: 'home',
                title: 'Home',
                path: '/home',
                element: <Home/>
            },
            {
                id: 'userinfo',
                title: 'userinfo',
                path: '/userinfo',
                element: <PersonCenter/>
            }
        ]
    },
    {
        id: 'login',
        title: '登录',
        path: BASE_LOGIN_URL,
        element: <Login/>,
    },
    {
        id: '404',
        title: '404',
        path: '/*',
        component: <NotFound/>,

    }

]
export const router: Router = createBrowserRouter(commonRouter);


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
export const parseRouters = (menuProps: Menu[]): RouterType[] => {

    if (menuProps === undefined || menuProps.length === 0) {
        return [];
    }
    return menuProps.map((item) => {
        return {
            id: item.id,
            title: item.permissionName,
            path: item.routePath ?? '',
            children: item.children ? parseRouters(item.children) : [],
            element: item.componentPath && lazyElement(item.componentPath),
        }
    })
}
