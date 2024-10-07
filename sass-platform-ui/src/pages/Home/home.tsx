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

import React, {useEffect, useState} from 'react';
import {DesktopOutlined, HomeOutlined, UserOutlined} from '@ant-design/icons';
import {Breadcrumb, Layout, Menu, MenuProps} from 'antd';
import withAuth from "@/hooks/Auth";
import "./index.scss"
import Sider from "antd/es/layout/Sider";
import {Content, Header} from "antd/es/layout/layout";
import {Outlet, useNavigate} from "react-router-dom";
import {fetchUserinfo} from "@/store/modules/user";
import {Userinfo} from "@/model/user";
import {useAppDispatch, useAppSelector} from "@/store";

type MenuItem = Required<MenuProps>['items'][number];

function getMenuItem(
    label: React.ReactNode,
    key: React.Key,
    icon?: React.ReactNode,
    children?: MenuItem[],
): MenuItem {
    return {
        key,
        icon,
        children,
        label,
    } as MenuItem;
}

const MENU_ITEMS: MenuItem[] = [
    getMenuItem('首页', '/', <HomeOutlined/>),
    getMenuItem('系统管理', 'system', <DesktopOutlined/>, [
        getMenuItem('用户管理', 'system/user', <UserOutlined/>)
    ]),
];


const Home: React.FC = withAuth(() => {

    const navigate = useNavigate();
    const [collapsed, setCollapsed] = useState(false);

    const dispatch = useAppDispatch();
    useEffect(() => {
        dispatch(fetchUserinfo());
    }, [dispatch])
    const realName = useAppSelector((state: { user: { userinfo: Userinfo }; }) => state.user.userinfo.realName);

    // 点击菜单时进行跳转
    const onMenuClick = (item: MenuItem) => {
        const path = item?.key?.toLocaleString();
        navigate(path!);
    }


    return (
        <div className="container">
            <Layout className="layout-container">
                <Sider className='layout-sider' collapsible collapsed={collapsed}
                       onCollapse={(value) => setCollapsed(value)}>
                    <Menu className="layout-menu" theme='dark' defaultSelectedKeys={['1']} mode="inline"
                          items={MENU_ITEMS} onClick={onMenuClick}/>
                </Sider>
                <Layout>
                    <Header className="layout-header">
                        <Breadcrumb/>
                        <div className="header-userinfo">
                            <UserOutlined/>
                            <span>{realName}</span>
                            <i className="arrow"/>
                        </div>
                    </Header>
                    <Content className="layout-content">
                        <Outlet/>
                    </Content>
                </Layout>
            </Layout>
        </div>
    );
});

export default Home;