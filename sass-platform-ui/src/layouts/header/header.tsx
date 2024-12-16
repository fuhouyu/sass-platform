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


import {Button, Col, Dropdown, Image, MenuProps, Row, Space} from "antd";
import {Bread, IconFont} from "@/components";
import {DownOutlined, LogoutOutlined, UserOutlined} from "@ant-design/icons";
import {Header as _Header} from "antd/es/layout/layout";
import {useEffect, useState} from "react";
import {useAppDispatch, useAppSelector} from "@/store";
import {fetchLogout, fetchUserinfo} from "@/store/modules/user";
import {Userinfo} from "@/model/user";
import {useNavigate} from "react-router-dom";
import type {ItemType} from "antd/es/menu/interface";
import './index.scss'
import {changeLanguage} from "@/store/modules/locale";
import i18n from "i18next";
import {useTranslation} from "react-i18next";
import {BASE_LOGIN_URL, BASE_USER_PROFILE_URL} from "@/constants/commonConstant";


export const Header = () => {

    const dispatch = useAppDispatch();
    const [language, setLanguage] = useState<string>(useAppSelector(state => state.locale.language));
    const {t} = useTranslation();
    useEffect(() => {
        dispatch(fetchUserinfo());
    }, [dispatch])
    const realName = useAppSelector((state: {
        user: { userinfo: Userinfo };
    }) => state.user.userinfo?.realName);

    const navigate = useNavigate();

    /**
     * 下拉选择框
     */
    const dropDownMenus: MenuProps['items'] = [
        {
            key: 'profile',
            label: t('Header.profile'),
            icon: <UserOutlined/>,
        },
        {
            key: 'logout',
            label: t('Header.logout'),
            icon: <LogoutOutlined/>,
        },
    ];

    // onClick
    const onDropDownClick: MenuProps['onClick'] = async (e: ItemType) => {
        if (!e) {
            return;
        }
        console.log(e.key)
        switch (e.key) {
            case 'logout':
                await dispatch(fetchLogout());
                navigate(BASE_LOGIN_URL);
                break;
            case 'profile':
                navigate(BASE_USER_PROFILE_URL);
                break;
        }
    };

    return (
        <>
            <_Header className="layout-header">
                <Row gutter={24} align={"middle"}>
                    <Col>
                        <Bread/>
                    </Col>
                    <Col className="user-header">
                        <Button
                            className='language-button'
                            onClick={async () => {
                                const switchLanguage: string = language === 'zh' ? 'en' : 'zh'
                                setLanguage(switchLanguage);
                                dispatch(changeLanguage(switchLanguage));
                                await i18n.changeLanguage(switchLanguage).then();
                            }}
                            icon={
                                <IconFont type={language === 'zh' ? 'i-en' : 'i-cn'}/>
                            }/>
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

            </_Header>
        </>
    )
}