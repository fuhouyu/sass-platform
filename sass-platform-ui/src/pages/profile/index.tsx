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


import {useState} from "react";
import "./index.scss"
import {SettingOutlined, UserOutlined} from "@ant-design/icons";
import {Avatar, Divider, Menu, message, Space, Tooltip} from "antd";
import {useTranslation} from "react-i18next";
import {useUserStore} from "@/store";
import {IconFont, S3Upload} from "@/components";
import Layout, {Content, Header} from "antd/es/layout/layout";
import type {MenuItemType} from "antd/es/menu/interface";
import {Userinfo} from "@/pages/profile/components/Userinfo.tsx";
import {AccountsSetting} from "@/pages/profile/account/AccountsSetting.tsx";
import {useResourceAction} from "@/hooks/useResourceAction.tsx";

interface MenuItem extends MenuItemType {
    element: React.ReactNode
}

/**
 * 个人中心用户详情
 * @constructor 构造函数
 */
export const UserProfile: React.FC = () => {
    const {t} = useTranslation();
    const {userinfo, fetchEditUserinfo} = useUserStore(state => state);
    const {preview} = useResourceAction();
    const menuItems: MenuItem[] = [
        {
            key: 'profile',
            icon: <UserOutlined/>,
            label: t('Menu.profile'),
            element: <Userinfo/>,
        },
        {
            key: 'accounts',
            icon: <SettingOutlined/>,
            label: t('Menu.accountsSetting'),
            element: <AccountsSetting/>,
        },
        // 可以继续添加其他菜单项
    ];
    const [selectedMenu, setSelectedMenu] = useState<MenuItem>(menuItems[0]);

    /**
     * 验证是否为图片
     * @param file 文件
     */
    const beforeUpload = async (file: File) => {
        const isImage = file.type.startsWith('image/');
        if (!isImage) {
            message.error('只能上传图片文件！');
        }
        return isImage;
    }

    return (
        <Layout className={'profile-container'}>
            <div className="profile-left">


                <div style={{textAlign: 'center'}}>

                    <S3Upload
                        accept={'image/*'}
                        prefix={"user-avatar"}
                        isPublic={true}
                        showUploadList={false}
                        beforeUpload={beforeUpload}
                        onUploadSuccess={async (resourceId) => {
                            await fetchEditUserinfo({...userinfo, avatar: resourceId});
                        }}
                    >
                        <Tooltip
                            className={'cursor-point'}
                            title={t('User.updateAvatar')}>
                            <Avatar
                                size={{xs: 100, sm: 100, md: 100, lg: 100, xl: 100, xxl: 100}}
                                icon={<UserOutlined/>}
                                src={preview(userinfo.avatar)}
                                className="avatar"
                            />
                        </Tooltip>
                    </S3Upload>
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
                    defaultSelectedKeys={[selectedMenu.key.toString()]}
                    items={menuItems}
                    onClick={(menu) => {
                        setSelectedMenu(menuItems.find(item => item.key === menu.key) as MenuItem);
                    }}
                />
            </div>
            <div className="profile-right">
                <Content>
                    <Header className="layout-header">
                        <h2 className="profile-right-title">
                            {selectedMenu.label}
                        </h2>
                    </Header>
                    <Divider/>
                    {selectedMenu.element}
                </Content>
            </div>
        </Layout>

    );
}

