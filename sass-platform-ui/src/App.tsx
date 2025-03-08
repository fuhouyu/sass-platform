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


import React, {useEffect, useState} from "react";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import {commonRoutes} from "@/routes/routes.tsx";
import '@/i18n/index'
import {useRoutes} from "@/hooks/useRoutes.tsx";
import {ConfigProvider} from "antd";
import {useLocaleStore, useRouterStore} from "@/store";
import {Locale} from "antd/es/locale";
import enUS from 'antd/locale/en_US';
import zhCN from 'antd/locale/zh_CN';
import {CommonConstant} from "./constants/commonConstant";
import {PageLoading} from "./components";

export const App: React.FC = () => {
    // 假设 useRoutes 是一个自定义钩子，返回路由是否加载完成
    const {initialized, dynamicRoutes} = useRoutes();
    const language = useLocaleStore(state => state.language);
    const [antdLocale, setAntdLocale] = useState<Locale>();
    const [isLoading, setIsLoading] = useState(true);
    const {router, storeRouter} = useRouterStore(state => state);

    useEffect(() => {
        if (initialized) {
            const rootRoutes = [...commonRoutes];
            rootRoutes[0].children = [...dynamicRoutes, ...(rootRoutes[0].children ?? [])];
            const updatedRouter = createBrowserRouter(rootRoutes);
            setIsLoading(false);
            storeRouter(updatedRouter)

        }
    }, [initialized, dynamicRoutes, storeRouter]);


    useEffect(() => {
        setAntdLocale(language === CommonConstant.ZH_CN_LANGUAGE ? zhCN : enUS);
    }, [language]);

    if (isLoading) {
        return <PageLoading/>;
    }


    // 路由加载完成后，渲染页面
    return (
        <ConfigProvider
            locale={antdLocale}
            theme={{
                components: {
                    Tree: {
                        titleHeight: 32,
                    },
                },
            }}
        >
            <RouterProvider router={router!}/>
        </ConfigProvider>
    );
};