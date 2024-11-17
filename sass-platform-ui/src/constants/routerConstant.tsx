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

import Login from "@/pages/Login";
import Home from "@/pages/Home";
import {PersonCenter} from "@/pages/Userinfo/personCenter";
import User from "@/pages/System/User/user";
import {Tenant} from "@/pages/Tenant/tenant";
import React from "react";

export type RouterType = {
    title: string,
    path: string,
    element?: React.JSX.Element,
    children?: RouterType[]
}
export const RoutersConstant: RouterType[] = [
    {
        title: '登录',
        path: '/login',
        element: <Login/>,
    },
    {
        title: '首页',
        path: '/',
        element: <Home/>,
        children: [
            {
                title: '用户详情',
                path: '/userinfo',
                element: <PersonCenter/>
            },
            {
                title: '系统管理',
                path: '/system',
                children: [
                    {
                        title: '用户管理',
                        path: '/system/user',
                        element: <User/>
                    }
                ]
            },
            {
                title: '租户管理',
                path: '/tenant',
                element: <Tenant/>
            }
        ]
    },

]