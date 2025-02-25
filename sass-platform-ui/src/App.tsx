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
import {RouterProvider} from "react-router-dom";
import {router} from "@/routes/routers";
import {PageLoading} from "@components/PageLoading/pageLoading";
import '@/i18n/index'
import {useRoutes} from "@/hooks/useRoutes.tsx";
import {ConfigProvider} from "antd";
import {useLocaleStore} from "@/store";
import {Locale} from "antd/es/locale";
import enUS from 'antd/locale/en_US';
import zhCN from 'antd/locale/zh_CN';
import {CommonConstant} from "./constants/commonConstant";

export const App: React.FC = () => {
    const initialize = useRoutes();
    const language = useLocaleStore(state => state.language);
    const [antdLocale, setAntdLocale] = useState<Locale>();
    useEffect(() => {
        setAntdLocale(language === CommonConstant.ZH_CN_LANGUAGE ? zhCN : enUS);
    }, [language])
    if (!initialize) {
        return <PageLoading/>;
    }
    return (
        <ConfigProvider
            locale={antdLocale}
            theme={{
                components: {
                    Tree: {
                        titleHeight: 32,
                    },
                }
            }}
        >
            <RouterProvider router={router} fallbackElement={<PageLoading/>}/>
        </ConfigProvider>
    );
};