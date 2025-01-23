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

import {Layout as AntdLayout} from "antd";
import {Content} from "antd/es/layout/layout";
import {Outlet, useLocation} from "react-router-dom";
import {Menu} from "@/layouts/menu/menu";
import {Header} from "@/layouts/header/header";
import './index.scss'
import {useEffect} from "react";
import {router} from "@/routes/routers.tsx";
import {BASE_LOGIN_URL} from "@/constants/commonConstant.tsx";
import useAuth from "@/hooks/useAuth.tsx";

export const Layout = () => {
    const accessToken = useAuth();
    const pathname = useLocation().pathname;
    useEffect(() => {
        if (!accessToken) {
            router.navigate(BASE_LOGIN_URL, {state: {from: pathname}}).then();
        }
    }, [accessToken]);
    return (
            <AntdLayout className="layout-container">
                <Menu/>
                <AntdLayout>
                    <Header/>
                    <Content className="layout-content">
                        <Outlet/>
                    </Content>
                </AntdLayout>
            </AntdLayout>
    )
}