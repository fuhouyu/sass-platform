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
import Login from "@/pages/Login";
import Home from "@/pages/Home";
import User from "@/pages/System/User/user.tsx";
import {PersonCenter} from "@/pages/Userinfo/personCenter";


const Routes: Router = createBrowserRouter([
    {
        path: '/login',
        element: <Login/>,
    },
    {
        path: '/',
        element: <Home/>,
        children: [
            {
                path: '/userinfo',
                element: <PersonCenter/>
            },
            {
                path: '/system',
                children: [
                    {
                        path: '/system/user',
                        element: <User/>
                    }
                ]
            }
        ]
    },

]);

export default Routes;