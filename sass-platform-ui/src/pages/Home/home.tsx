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

import React, {useCallback, useEffect, useState} from 'react';
import {DownOutlined, LogoutOutlined, UserOutlined} from '@ant-design/icons';
import {Col, Divider, Dropdown, Image, Layout, Menu, MenuProps, Row, Space} from 'antd';
import withAuth from "@/components/Auth/withAuth";
import "./index.scss"
import Sider from "antd/es/layout/Sider";
import {Content, Header} from "antd/es/layout/layout";
import {Outlet, useNavigate} from "react-router-dom";
import {fetchLogout, fetchUserinfo} from "@/store/modules/user";
import {UserModel} from "@/model/user";
import {useAppDispatch, useAppSelector} from "@/store";
import {MenuInfo} from "rc-menu/lib/interface";
import {PermissionModel} from "@/model/permissionModel";
import {getUserPermissionApi} from "@/apis/permission";
import {Bread, IconFont} from "@/components";

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

    const convertMenuItem = useCallback((permissionInterfaces: PermissionModel[]): MenuItem[] | null => {
        if (permissionInterfaces === undefined || permissionInterfaces.length === 0) {
            return null;
        }
        return permissionInterfaces?.map((item: PermissionModel) => {
            return {
                key: item.routePath!,
                label: item.permissionName,
                icon: item.icon ?
                    <IconFont type={item.icon} style={{fontSize: '16px'}}/> : undefined,
                children: item.children ? convertMenuItem(item.children) ?? null : null
            }
        })
    }, [])


    useEffect(() => {
        getUserPermissionApi().then((res: PermissionModel[]) => {
            let itemMenus = convertMenuItem(res);
            itemMenus = itemMenus ? itemMenus : [];
            setMenuItems(itemMenus);
        });
    }, [convertMenuItem])
    const navigate = useNavigate();
    const [collapsed, setCollapsed] = useState(false);

    const dispatch = useAppDispatch();
    useEffect(() => {
        dispatch(fetchUserinfo());
    }, [dispatch])
    const realName = useAppSelector((state: {
        user: { userinfo: UserModel };
    }) => state.user.userinfo?.realName);

    // 点击菜单时进行跳转
    const onMenuClick = (item: MenuItem) => {
        const path = item?.key?.toLocaleString();
        navigate(path!);
    }

    // onClick
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
                    <h3 className="platform-title">
                        Sass 平台
                    </h3>
                    <Divider/>
                    <Menu className="layout-menu" theme='dark' defaultSelectedKeys={['1']} mode="inline"
                          items={menuItems} onClick={onMenuClick}/>
                </Sider>
                <Layout>
                    <Header className="layout-header">
                        <Row gutter={24} align={"middle"}>
                            <Col className="user-header">
                                <div>
                                      <span className="tenant">
                                    我的租户
                               <IconFont type='i-24gl-swapHorizontal3'/>
                               </span>
                                    <Dropdown menu={{
                                        items: menus,
                                        onClick: onDropDownClick
                                    }}>
                           <span>

                                <Space>
                                    你好, {realName}
                                    <Image
                                        className="avatar"
                                        src="error"
                                        preview={false}
                                        fallback="https://img.fuhouyu.com/2.jpeg"
                                    />
                                    <DownOutlined/>
                                </Space>
                           </span>
                                    </Dropdown>
                                </div>
                            </Col>
                        </Row>
                    </Header>
                    <Divider/>
                    <Row className="bread-row">
                        <Col span={21} className="layout-bread">
                            <Bread/>
                        </Col>
                    </Row>
                    <Content className="layout-content">
                        <Outlet/>
                    </Content>
                </Layout>
            </Layout>
        </div>
    );
});

export default Home;