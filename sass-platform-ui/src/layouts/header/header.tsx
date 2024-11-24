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


import {Col, Divider, Dropdown, Image, MenuProps, Row, Space} from "antd";
import {Bread, IconFont} from "@/components";
import {DownOutlined, LogoutOutlined, UserOutlined} from "@ant-design/icons";
import {Header as _Header} from "antd/es/layout/layout";
import {useEffect} from "react";
import {useAppDispatch, useAppSelector} from "@/store";
import {fetchLogout, fetchUserinfo} from "@/store/modules/user";
import {UserModel} from "@/model/user";
import {useNavigate} from "react-router-dom";
import type {ItemType} from "antd/es/menu/interface";
import './index.scss'

/**
 * 下拉选择框
 */
const dropDownMenus: MenuProps['items'] = [
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

export const Header = () => {

    const dispatch = useAppDispatch();
    useEffect(() => {
        dispatch(fetchUserinfo());
    }, [dispatch])
    const realName = useAppSelector((state: {
        user: { userinfo: UserModel };
    }) => state.user.userinfo?.realName);

    const navigate = useNavigate();

    // onClick
    const onDropDownClick: MenuProps['onClick'] = (e: ItemType) => {
        if (!e) {
            return;
        }
        switch (e.key) {
            case 'logout':
                dispatch(fetchLogout());
                navigate('/login');
                break;
            case 'userinfo':
                navigate('/userinfo');
                break;
        }
    };

    return (
        <>
            <_Header className="layout-header">
                <Row gutter={24} align={"middle"}>
                    <Col className="user-header">
                        <div>
                                      <span className="tenant">
                                    我的租户
                               <IconFont type='i-24gl-swapHorizontal3'/>
                               </span>
                            <Dropdown menu={{
                                items: dropDownMenus,
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
                {/*面包屑*/}
                <Divider/>
                <Row className="bread-row" gutter={24} align={"middle"}>
                    <Col span={24} className="layout-bread">
                        <Bread/>
                    </Col>
                </Row>
            </_Header>
        </>
    )
}