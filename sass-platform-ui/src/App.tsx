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
import '@/i18n/index'
import {useRoutes} from "@/hooks/useRoutes.tsx";
import {ConfigProvider, theme} from "antd";
import {useLocaleStore, useRouterStore} from "@/store";
import {Locale} from "antd/es/locale";
import enUS from 'antd/locale/en_US';
import zhCN from 'antd/locale/zh_CN';
import {CommonConstant} from "./constants/commonConstant";
import {PageLoading} from "./components";
import {useThemeStore} from "@/store/modules/theme.tsx";
import 'dayjs/locale/zh-cn';
import dayjs from "dayjs";
import * as echarts from "echarts/core";

echarts.registerTheme('dark-white-font', {
    backgroundColor: '#2A2A2A',
    textStyle: {
        color: '#ffffff'
    },
    title: {
        textStyle: {
            color: '#ffffff'
        },
        subtextStyle: {
            color: '#cccccc'
        }
    },
    legend: {
        textStyle: {
            color: '#ffffff'
        }
    },
    tooltip: {
        backgroundColor: '#333333', // 深色背景
        borderColor: '#555555',
        borderWidth: 1,
        textStyle: {
            color: '#ffffff'        // 白色字体
        }
    },
    xAxis: {
        axisLabel: {
            color: '#ffffff'
        },
        axisLine: {
            lineStyle: {
                color: '#888888'
            }
        }
    },
    yAxis: {
        axisLabel: {
            color: '#ffffff'
        },
        axisLine: {
            lineStyle: {
                color: '#888888'
            }
        },
        splitLine: {
            lineStyle: {
                color: '#333333'
            }
        }
    }
});
export const App: React.FC = () => {
    const {initialized, updateDynamicRoutes} = useRoutes();
    const language = useLocaleStore(state => state.language);
    const [antdLocale, setAntdLocale] = useState<Locale>();
    const {router} = useRouterStore(state => state);
    const currentTheme = useThemeStore(state => state.theme);



    useEffect(() => {
        updateDynamicRoutes().then();
        dayjs.locale(language);
        setAntdLocale(language === CommonConstant.ZH_CN_LANGUAGE ? zhCN : enUS);
    }, [language, updateDynamicRoutes]);

    if (!initialized) {
        return <PageLoading/>;
    }
    // 路由加载完成后，渲染页面
    return (
        <ConfigProvider
            locale={antdLocale}
            theme={{
                token: {
                    colorBgBase: currentTheme === 'dark' ? "#161616" : '#ffffff'
                },
                algorithm: currentTheme === 'dark' ? theme.darkAlgorithm : theme.defaultAlgorithm,
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