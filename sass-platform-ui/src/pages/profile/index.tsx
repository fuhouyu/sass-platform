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


import React from "react";
import "./index.scss"
import {SettingOutlined, UserOutlined} from "@ant-design/icons";
import {Avatar, Divider, Menu, Space} from "antd";
import {useTranslation} from "react-i18next";
import {useUserStore} from "@/store";
import {IconFont} from "@/components";
import Layout, {Content, Header} from "antd/es/layout/layout";
import {Outlet, useNavigate} from "react-router-dom";
import type {MenuItemType} from "antd/es/menu/interface";


/**
 * 个人中心用户详情
 * @constructor 构造函数
 */
export const UserProfile: React.FC = () => {
    const {t} = useTranslation();
    const {userinfo} = useUserStore(state => state);
    const navigate = useNavigate();
    const [headerTitle, setHeaderTitle] = React.useState<string>(t('Menu.profile'));

    const menuClick = (path: string, title: string) => {
        navigate(path);
        setHeaderTitle(title);
    }
    const menuItems: MenuItemType[] = [
        {
            key: 'profile',
            icon: <UserOutlined/>,
            label: t('Menu.profile'),
            onClick: () => menuClick('', t('Menu.profile'))
        },
        {
            key: 'accountsBinding',
            icon: <SettingOutlined/>,
            label: t('Menu.accountsBinding'),
            onClick: () => menuClick('accounts', t('Menu.accountsBinding'))
        },
        // 可以继续添加其他菜单项
    ];


    return (
        <Layout className={'profile-container'}>
            <div className="profile-left">
                <div style={{textAlign: 'center'}}>
                    <Avatar
                        size={{xs: 100, sm: 100, md: 100, lg: 100, xl: 100, xxl: 100}}
                        src={"https://oss.fuhouyu.com/2.jpeg"}
                    />
                    <div>
                        <h2 className="text-align-center">
                            {userinfo.realName}
                        </h2>
                    </div>
                </div>

                <div className={'profile-userinfo'}>
                    <ul>
                        <Space direction="vertical">
                            <li>
                                <Space>
                                    <IconFont
                                        type={'i-a-Identityshenfenzhiwei'}
                                        style={{fontSize: '.9rem'}}
                                    />
                                    <span>{userinfo.userPosition?.positionName}</span>
                                </Space>
                            </li>

                            <li>
                                <Space>
                                    <IconFont
                                        type={'i-IPdizhi'}
                                        style={{fontSize: '.9rem'}}
                                    />
                                    <span>{userinfo.loginIp}</span>
                                </Space>
                            </li>
                        </Space>
                    </ul>
                </div>
                <Divider/>
                <Menu
                    className={'profile-menu'}
                    mode="inline"
                    defaultSelectedKeys={['profile']}
                    items={menuItems}
                />
            </div>
            <div className="profile-right">
                <Content>
                    <Header className="layout-header">
                        <h2 className="profile-right-title">
                            {headerTitle}
                        </h2>
                    </Header>
                    <Divider/>
                    <Outlet/>
                </Content>
            </div>
        </Layout>

    )
}

