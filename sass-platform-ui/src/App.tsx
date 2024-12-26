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


import React, {useEffect, useState} from "react";
import {RouterProvider} from "react-router-dom";
import {parseRouters, router} from "@/routes/routers";
import {getAccessToken} from "@/utils";
import {useAppDispatch} from "@/store";
import {fetchUserMenus} from "@/store/modules/user";
import {Menu} from "@/model/menu";
import {PageLoading} from "@components/PageLoading/pageLoading";
import '@/i18n/index'
import NotFound from "@/pages/error/notfound";
import {BASE_LOGIN_URL, BASE_REDIRECT_URL} from "@/constants/commonConstant";

export const App: React.FC = () => {
    const dispatch = useAppDispatch();
    const [loading, setLoading] = useState(true);
    const pathname = location.pathname;
    useEffect(() => {
        if (pathname.includes(BASE_LOGIN_URL) || pathname.includes(BASE_REDIRECT_URL)) {
            setLoading(false);
            return;
        }
        const accessToken = getAccessToken();
        if (!accessToken) {
            router.navigate(BASE_LOGIN_URL, {state: {from: router.state.location.pathname}}).then()
            setLoading(false);
            return;
        }
        dispatch(fetchUserMenus())
            .then((userMenus: Menu[]) => {
                setLoading(false);
                router.routes[0]?.children!.push(...parseRouters(userMenus))
            });
    }, [dispatch])
    if (loading) {
        return <PageLoading/>
    }
    return (
        <RouterProvider
            fallbackElement={<NotFound/>}
            router={router}/>

    );
}