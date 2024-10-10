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
import {DownOutlined, HomeOutlined, LogoutOutlined, UserOutlined} from '@ant-design/icons';
import {Breadcrumb, Dropdown, Layout, Menu, MenuProps, Space} from 'antd';
import withAuth from "@/components/withAuth";
import "./index.scss"
import Sider from "antd/es/layout/Sider";
import {Content, Header} from "antd/es/layout/layout";
import {Outlet, useNavigate} from "react-router-dom";
import {fetchLogout, fetchUserinfo} from "@/store/modules/user";
import {UserinfoInterface} from "@/model/user";
import {useAppDispatch, useAppSelector} from "@/store";
import {MenuInfo} from "rc-menu/lib/interface";
import {PermissionInterface} from "@/model/permission";
import {getUserPermissionApi} from "@/apis/permission";
import {IconFont} from "@components/iconFont";

type MenuItem = Required<MenuProps>['items'][number];

const menus: MenuProps['items'] = [
    {
        key: 'userinfo',
        label: '个人中心',
        icon: <UserOutlined/>,
    },
    {
        key: 'logout',
        label: '退出',
        icon: <LogoutOutlined/>,
    },
];


const Home: React.FC = withAuth(() => {

    const [menuItems, setMenuItems] = useState<MenuItem[]>([]);

    const convertMenuItem = (permissionInterfaces: PermissionInterface[]): MenuItem[] | null => {
        if (permissionInterfaces === undefined || permissionInterfaces.length === 0) {
            return null;
        }
        return permissionInterfaces?.map((item: PermissionInterface) => {
            return {
                key: item.routePath,
                label: item.permissionName,
                icon: item.icon ?
                    <IconFont type="i-setting" style={{fontSize: '16px'}}/> : undefined,
                children: convertMenuItem(item.children)
            }
        })
    }

    useEffect(() => {
        getUserPermissionApi().then((res: PermissionInterface[]) => {
            let itemMenus = convertMenuItem(res);
            itemMenus = itemMenus ? itemMenus : [];
            itemMenus.unshift({
                key: 'home',
                label: '首页',
                icon: <HomeOutlined style={{fontSize: '16px'}}/>,
            })
            setMenuItems(itemMenus);
        });
    }, [])
    const navigate = useNavigate();
    const [collapsed, setCollapsed] = useState(false);

    const dispatch = useAppDispatch();
    useEffect(() => {
        dispatch(fetchUserinfo());
    }, [dispatch])
    const realName = useAppSelector<UserinfoInterface>((state: {
        user: { userinfo: UserinfoInterface };
    }) => state.user.userinfo?.realName);

    // 点击菜单时进行跳转
    const onMenuClick = (item: MenuItem) => {
        const path = item?.key?.toLocaleString();
        navigate(path!);
    }


    const onDropDownClick: MenuProps['onClick'] = (e: MenuInfo) => {
        switch (e.key) {
            case 'logout':
                dispatch(fetchLogout());
                navigate('/login')
                break;
            case 'userinfo':
                navigate('/userinfo')
                break
        }
    };


    return (
        <div className="container">
            <Layout className="layout-container">
                <Sider className='layout-sider' collapsible collapsed={collapsed}
                       onCollapse={(value) => setCollapsed(value)}>
                    <Menu className="layout-menu" theme='dark' defaultSelectedKeys={['1']} mode="inline"
                          items={menuItems} onClick={onMenuClick}/>
                </Sider>
                <Layout>
                    <Header className="layout-header">
                        <Breadcrumb/>
                        <div>
                            <Dropdown menu={{
                                items: menus,
                                onClick: onDropDownClick
                            }}>
                           <span>
                                <Space>
                                    你好, {realName} <DownOutlined/>
                                </Space>
                           </span>
                            </Dropdown>
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