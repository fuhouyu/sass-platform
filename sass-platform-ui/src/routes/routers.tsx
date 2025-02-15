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

import {createBrowserRouter, createHashRouter} from "react-router-dom";
import type {Router} from "@remix-run/router/dist/router";
import React from "react";
import {NotFound} from "@/pages/error/notfound/NotFound";
import {Home} from "@/pages/home/Home";
import {UserProfile} from "@/pages/profile";
import Login from "@/pages/login";
import {
    BASE_HOME_URL,
    BASE_LOGIN_URL,
    BASE_PORTAL_URL,
    BASE_REDIRECT_URL,
    BASE_USER_PROFILE_URL
} from "@/constants/commonConstant";
import MainPortal from "@/pages/portal";
import {PostThirdPartyRedirect} from "@/pages/redirect/PostThirdPartyRedirect.tsx";
import {LoaderFunction} from "@remix-run/router/utils.ts";
import {getAccessToken} from "@/utils";
import {AccountBind} from "@/pages/profile/account/AccountBind.tsx";
import {LayoutMain} from "@/pages/Layout";

export type RouterType = {
    id: string;
    title: string,
    path: string,
    element: React.ReactNode | null | undefined,
    component?: React.ReactNode,
    children?: RouterType[];
    loader?: LoaderFunction | boolean;
}


/**
 * 公共路由
 */
export const commonRouter: RouterType[] = [

    {
        id: 'layout',
        title: 'dashboard',
        path: '/',
        element: <LayoutMain/>,
        loader: async () => {
            const accessToken = getAccessToken();
            if (!accessToken) {
                throw new Response(null, {status: 302, headers: {Location: "/login"}});
            }
            return true;
        },
        children: [
            {
                id: 'home',
                title: 'Home',
                path: BASE_HOME_URL,
                element: <Home/>
            },
            {
                id: 'profile',
                title: 'profile',
                path: BASE_USER_PROFILE_URL,
                element: <UserProfile/>,
            }
        ]
    },
    {
        id: 'login',
        title: 'login',
        path: BASE_LOGIN_URL,
        element: <Login/>,
    },
    {
        id: 'portal',
        title: 'portal',
        path: BASE_PORTAL_URL,
        element: <MainPortal/>,
    },
    {
        id: 'redirect',
        title: 'redirect',
        path: BASE_REDIRECT_URL,
        element: <PostThirdPartyRedirect/>,

    },
    {
        id: 'account-bind',
        title: '账号绑定',
        path: '/account-bind',
        element: <AccountBind/>
    },
    {
        id: '404',
        title: '404',
        path: '/*',
        element: <NotFound/>,
    }

]
export const router: Router =
    import.meta.env.VITE_ROUTE_TYPE === 'HASH' ? createHashRouter(commonRouter) : createBrowserRouter(commonRouter);

