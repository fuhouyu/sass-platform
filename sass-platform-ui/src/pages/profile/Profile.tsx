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


import React, {useEffect, useState} from "react";
import "./index.scss"
import {AntDesignOutlined, SettingOutlined, UserOutlined} from "@ant-design/icons";
import {Avatar, Button, Form, Input, message, Radio} from "antd";
import {useAppDispatch, useAppSelector} from "@/store";
import {Userinfo as UserinfoModal} from "@/model/user";
import {fetchEditUserinfo} from "@/store/modules/user.tsx";
import {useTranslation} from "react-i18next";
import {AccountSettings} from "@/pages/profile/account/AccountSettings.tsx";

interface MenuLiInterface {
    key: string;
    icon: React.ReactElement;
    label: string;
}

interface UserinfoFormInterface {
    label: string;
    key: string;
    value: string;
    disabled: boolean;
}
/**
 * 个人中心用户详情
 * @constructor 构造函数
 */
export const Profile: React.FC = () => {
    const {t} = useTranslation();
    const menuItems: MenuLiInterface[] = [
        {key: 'userinfo', icon: <UserOutlined/>, label: t('Menu.profile')},
        {key: 'accountSettings', icon: <SettingOutlined/>, label: t('Menu.accountSettings')},
        // 可以继续添加其他菜单项
    ];

    const [selectedMenuInterface, setSelectedMenuInterface] = useState<MenuLiInterface>(menuItems[0]);

    const handleClick = (item: MenuLiInterface) => {
        setSelectedMenuInterface(item); // 更新选中项的索引
    };

    const userinfo: UserinfoModal = useAppSelector((state: {
        user: { userinfo: UserinfoModal }
    }) => state.user.userinfo);

    const formItem: UserinfoFormInterface[] = [
        {key: 'username', label: t('User.username'), value: userinfo.username!, disabled: true},
        {key: 'realName', label: t('User.realName'), value: userinfo.realName!, disabled: false},
        {key: 'nickname', label: t('User.nickname'), value: userinfo.nickname!, disabled: false},
        {key: 'email', label: t('User.email'), value: userinfo.email!, disabled: false},
        {key: 'loginDate', label: t('User.loginDate'), value: userinfo.loginDate!, disabled: true},
        {key: 'loginIp', label: t('User.loginIp'), value: userinfo.loginIp!, disabled: true},
    ]
    const dispatch = useAppDispatch();
    const [form] = Form.useForm();
    useEffect(() => {
        form.setFieldsValue({...userinfo});
    }, [form, userinfo]);

    const [buttonLoading, setButtonLoading] = useState<boolean>(false);

    const onCancel = (): void => {
        setButtonLoading(true)
        form.setFieldsValue({...userinfo});
        setButtonLoading(false);
    }


    const onFinish = (values: UserinfoModal): void => {
        setButtonLoading(true);
        dispatch(fetchEditUserinfo(values))
            .then(() => {
                form.setFieldsValue({...values});
                setButtonLoading(false);
                message.success('修改成功').then();
            }).catch((error: Error) => {
            message.error('用户修改失败' + error.message).then();
        })
    }


    return (
        <div className="profile-container">
            <div className="profile-left">
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
                <div className="profile-menu">
                    {
                        menuItems.map((item: MenuLiInterface) => (
                            <Button key={item.key}
                                    type={selectedMenuInterface.key === item.key ? 'primary' : 'default'}
                                    className={'menu-button'}
                                    onClick={() => {
                                        handleClick(item)
                                    }}
                            >
                                {item.icon} {item.label}
                            </Button>
                        ))
                    }
                </div>
            </div>
            <div className="profile-right">
                <div className="profile-right-title">
                    {selectedMenuInterface.label}
                </div>
                {
                    selectedMenuInterface.key === 'userinfo' ? <Form className="profile-form"
                                                                     form={form}
                                                                     name="basic"
                                                                     labelCol={{span: 8}}
                                                                     wrapperCol={{span: 16}}
                                                                     style={{maxWidth: 600}}
                                                                     onFinish={onFinish}
                                                                     disabled={buttonLoading}
                                                                     autoComplete="off"
                    >
                        {formItem.map((item: UserinfoFormInterface,) => (
                            <Form.Item
                                label={item.label}
                                name={item.key}
                                key={item.key}
                            >
                                <Input disabled={item.disabled} key={item.key}/>
                            </Form.Item>
                        ))}

                        <Form.Item name="gender" key="gender" label={t('User.gender')}>
                            <Radio.Group>
                                <Radio value='male'>{t('User.male')}</Radio>
                                <Radio value='female'>{t('User.female')}</Radio>
                            </Radio.Group>
                        </Form.Item>

                        <Form.Item className="profile-submit text-align-center" wrapperCol={{offset: 8, span: 16}}>
                            <Button type="primary" htmlType="submit" loading={buttonLoading}>
                                {t('Button.confirm')}
                            </Button>
                            <Button type="primary" danger onClick={onCancel}>
                                {t('Button.cancel')}
                            </Button>
                        </Form.Item>
                    </Form> : <AccountSettings/>
                }
            </div>

        </div>
    )
}

