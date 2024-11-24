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

import {Layout as _Layout} from "antd";
import {Content} from "antd/es/layout/layout";
import {Outlet} from "react-router-dom";
import {Menu} from "@/layouts/menu/menu";
import {Header} from "@/layouts/header/header";
import './index.scss'
import withAuth from "@components/Auth/withAuth";

export const Layout = withAuth(() => {
    return (
        <>
            <_Layout className="layout-container">
                <Menu/>
                <_Layout>
                    <Header/>
                    <Content className="layout-content">
                        <Outlet/>
                    </Content>
                </_Layout>
            </_Layout>
        </>
    )
})