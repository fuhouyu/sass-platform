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


import React, {useState} from "react";
import "./index.scss"
import {AntDesignOutlined, SettingOutlined, UserOutlined} from "@ant-design/icons";
import {Avatar} from "antd";
import {useAppSelector} from "@/store";
import {UserinfoInterface} from "@/model/user";
import {Userinfo} from "@/pages/Userinfo/userinfo";

interface MenuLiInterface {
    key: string;
    icon: React.ReactElement;
    label: string;
}

/**
 * 个人中心用户详情
 * @constructor 构造函数
 */
export const PersonCenter: React.FC = () => {

    const userinfo = useAppSelector<UserinfoInterface>((state: {
        user: { userinfo: UserinfoInterface };
    }) => state.user.userinfo);

    const menuItems: MenuLiInterface[] = [
        {key: 'userinfo', icon: <UserOutlined/>, label: '个人资料'},
        {key: 'accountSettings', icon: <SettingOutlined/>, label: '账号设置'},
        // 可以继续添加其他菜单项
    ];

    const [selectedMenuInterface, setSelectedMenuInterface] = useState<MenuLiInterface>(menuItems[0]);


    const handleClick = (item: MenuLiInterface) => {
        setSelectedMenuInterface(item); // 更新选中项的索引
    };


    return (
        <>
            <div className="userinfo-container">
                <div className="userinfo-left">
                    <div>
                        <Avatar
                            size={{xs: 100, sm: 100, md: 100, lg: 100, xl: 100, xxl: 100}}
                            src={userinfo.avatar}
                            icon={<AntDesignOutlined/>}
                        />
                        <p className="text-align-center">
                            您好，{userinfo.realName}
                        </p>
                    </div>
                    <ul className="userinfo-menu">
                        {
                            menuItems.map((item: MenuLiInterface) => (
                                <li key={item.key}
                                    className={`menu-li ${selectedMenuInterface.key === item.key ? 'menu-li-selected' : ''}`}
                                    onClick={() => {
                                        handleClick(item)
                                    }}
                                >
                                    <span>
                                        {item.icon} {item.label}
                                    </span>

                                </li>
                            ))
                        }
                    </ul>
                </div>
                <div className="userinfo-right">
                    <div className="userinfo-right-title">
                        {selectedMenuInterface.label}
                    </div>
                    <Userinfo/>
                </div>
            </div>
        </>
    )
}