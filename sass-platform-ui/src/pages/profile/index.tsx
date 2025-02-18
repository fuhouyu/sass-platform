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


import React, {useState} from "react";
import "./index.scss"
import {SettingOutlined, UploadOutlined, UserOutlined} from "@ant-design/icons";
import {Avatar, Divider, Menu, message, Space, Tooltip, Upload} from "antd";
import {useTranslation} from "react-i18next";
import {useUserStore} from "@/store";
import {IconFont} from "@/components";
import Layout, {Content, Header} from "antd/es/layout/layout";
import type {MenuItemType} from "antd/es/menu/interface";
import {Userinfo} from "@/pages/profile/components/Userinfo.tsx";
import {AccountsBinding} from "@/pages/profile/account/AccountsBinding.tsx";
import {resourceApi} from "@/apis/resource.tsx";
import {RcFile} from "antd/es/upload";
import {UploadRequestFile} from "rc-upload/lib/interface";
import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";

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
            label: t('Menu.accountsBinding'),
            element: <AccountsBinding/>,
        },
        // 可以继续添加其他菜单项
    ];
    const [selectedMenu, setSelectedMenu] = useState<MenuItem>(menuItems[0]);

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
                <Upload
                    customRequest={async (options) => {
                        const resourcePresignedUrlResponse = await resourceApi.generateResourcePresignedUrl({
                            businessName: 'user-avatar',
                            method: 'PUT',
                        });
                        const {file}: { file: UploadRequestFile | RcFile } = options;
                        const fileToUpload = file as RcFile;
                        try {
                            await fetch(resourcePresignedUrlResponse.presignedUrl, {
                                method: 'PUT',
                                body: fileToUpload,
                                headers: {
                                    'Content-Type': fileToUpload.type,
                                },
                            });
                            const resourceId = await resourceApi.saveInfoApi({
                                name: fileToUpload.name,
                                size: fileToUpload.size,
                                mimeType: fileToUpload.type,
                                isPublic: true,
                                version: 1,
                                objectKey: resourcePresignedUrlResponse.objectKey,
                            });
                            await fetchEditUserinfo({...userinfo, avatar: resourceId});
                            message.success(t('User.updateAvatarSuccess'));
                        } catch {
                            message.error(t('User.updateAvatarError'));
                        }

                    }}
                    showUploadList={false}
                    beforeUpload={beforeUpload}
                >
                    <Tooltip className={'avatar-upload-button'} placement="top"
                             title={t('User.updateAvatar')}>
                        <UploadOutlined style={{fontSize: '20px', color: '#1890ff'}}/>
                    </Tooltip>
                </Upload>

                <div style={{textAlign: 'center'}}>

                <Avatar
                        size={{xs: 100, sm: 100, md: 100, lg: 100, xl: 100, xxl: 100}}
                        src={userinfo.avatar ? `${import.meta.env.VITE_API_URL}/${BaseUrlConstant.RESOURCE_API_PREFIX}/preview/${userinfo.avatar}` : ''}
                        className="avatar"
                    >

                    </Avatar>
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

    )
}

