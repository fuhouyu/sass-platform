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


import Sider from "antd/es/layout/Sider";
import {Divider, Menu as _Menu} from 'antd';
import {useState} from "react";
import {MenuType, useMenuTree} from "@/hooks/useMenuTree";
import {useNavigate} from "react-router-dom";
import './index.scss'

/**
 * 侧边菜单组件
 * @constructor 构造函数
 */
export const Menu = () => {
    const menuItems = useMenuTree<MenuType>();
    const navigate = useNavigate();
    const [collapsed, setCollapsed] = useState<boolean>(false);
    // 点击菜单时进行跳转
    const onMenuClick = (item: MenuType) => {
        const path = item?.key?.toLocaleString();
        navigate(path!);
    }
    return (
        <>
            <Sider className='layout-sider' collapsible collapsed={collapsed}
                   onCollapse={(value) => setCollapsed(value)}>
                <h3 className="platform-title">
                    Sass 平台
                </h3>
                <Divider/>
                <_Menu className="layout-menu" theme='dark' defaultSelectedKeys={['1']} mode="inline"
                       items={menuItems} onClick={onMenuClick}/>
            </Sider>
        </>
    )
}