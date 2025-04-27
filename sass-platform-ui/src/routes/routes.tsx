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

import {DataRouteObject} from "react-router-dom";
import {NotFound} from "@/pages/error/notfound/NotFound";
import {Home} from "@/pages/home/Home";
import {UserProfile} from "@/pages/profile";
import Login from "@/pages/login";
import {PostThirdPartyRedirect} from "@/pages/redirect/PostThirdPartyRedirect.tsx";
import {getAccessToken} from "@/utils";
import {AccountBind} from "@/pages/profile/account/AccountBind.tsx";
import {LayoutMain} from "@/pages/Layout";
import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";
import Office from "@/pages/office";
import {PageLoading} from "@/components";


/**
 * 公共路由
 */
export const commonRoutes: DataRouteObject[] = [

    {
        id: 'layout',
        path: '/',
        Component: LayoutMain,
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
                path: BaseUrlConstant.HOME_URL,
                Component: Home
            },
            {
                id: 'profile',
                path: BaseUrlConstant.USER_PROFILE_URL,
                Component: UserProfile,
            }
        ],
        hydrateFallbackElement: <PageLoading/>
    },
    {
        id: 'office-preview',
        path: `${BaseUrlConstant.OFFICE_URL}/:id`,
        Component: Office
    },
    {
        id: 'login',
        path: BaseUrlConstant.LOGIN_URL,
        Component: Login,
    },
    {
        id: 'redirect',
        path: BaseUrlConstant.REDIRECT_URL,
        Component: PostThirdPartyRedirect,

    },
    {
        id: 'account-bind',
        path: '/account-bind',
        Component: AccountBind
    },
    {
        id: '404',
        path: '/*',
        Component: NotFound,
    }

]
