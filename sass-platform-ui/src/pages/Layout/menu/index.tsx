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

import {useNavigate} from "react-router-dom";
import {useState} from "react";
import {useUserStore} from "@/store";
import {useTranslation} from "react-i18next";
import {MenuProps, useMenuTree} from "@/hooks/useMenuTree.tsx";
import {IconFont} from "@/components";
import {Menu as UserMenus, MenuType} from "@/model/menu.tsx";
import Sider from "antd/es/layout/Sider";
import {Divider, Menu} from "antd";
import './index.scss'
import {MenuFoldOutlined, MenuUnfoldOutlined} from "@ant-design/icons";

export const LayoutMenu = () => {
    const navigate = useNavigate();
    const [collapsed, setCollapsed] = useState<boolean>(false);
    const {t} = useTranslation();
    const rawPath = location.pathname.replace(/^\/+/, ''); // 去掉开头的 `/`
    const segments = rawPath.split('/').filter(Boolean);

    const defaultSelectedKeys = [rawPath];
    const defaultOpenKeys = segments.length > 1
        ? [segments.slice(0, -1).join('/')]
        : [];
    const commonMenus: MenuProps[] = [
        {
            key: 'home',
            title: t('Menu.home'),
            label: t('Menu.home'),
            icon:
                <IconFont type="i-zhuye" style={{fontSize: '1rem'}}/>,

        }
    ]

    const userMenus: UserMenus[] = useUserStore(state => state.userMenus) ?? []

    const menuItems: MenuProps[] = useMenuTree(userMenus, [MenuType.BUTTON]) as MenuProps[]
    menuItems.unshift(...commonMenus);

    return (
        <Sider className={'layout-main-sider'}
               collapsible
               collapsed={collapsed}
               theme={'light'}
               trigger={collapsed ? <MenuUnfoldOutlined/> : <MenuFoldOutlined/>}
               zeroWidthTriggerStyle={{
                   padding: 0
               }}
               onCollapse={(value) => setCollapsed(value)}>
            <Divider/>
            <Menu className="layout-menu"
                  defaultSelectedKeys={defaultSelectedKeys}
                  defaultOpenKeys={defaultOpenKeys}
                  mode="inline"
                  items={menuItems} onClick={({key}) => {
                navigate(key);
            }}/>
        </Sider>
    )
}