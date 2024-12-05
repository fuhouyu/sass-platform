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
import {Button, Divider, Form, Input, message} from "antd";
import {LockOutlined, UserOutlined} from '@ant-design/icons';
import {useLocation, useNavigate} from "react-router-dom";
import {fetchLogin, fetchUserMenus} from "@/store/modules/user";
import {useAppDispatch, useAppSelector} from "@/store";
import {UserAuthentication} from "@/model/authentication";
import useAuth from "@/hooks/useAuth";
import {AccountType} from "@/constants/accountTypeConstant";
import {parseRouters, router} from "@/routes/routers";
import {IconFont} from "@/components";
import {changeLanguage} from "@/store/modules/locale";
import {useTranslation} from "react-i18next";

/**
 * 登录组件
 * @constructor
 */
export const Login: React.FC = () => {
    const navigate = useNavigate();
    const [loginButtonLoading, setLoginButtonLoading] = useState<boolean>(false);
    const location = useLocation();
    const dispatch = useAppDispatch();
    const isAuth = useAuth();
    const {t, i18n} = useTranslation();
    const [language, setLanguage] = useState<string>(useAppSelector(state => state.locale.language));

    // 如果本身存在token，跳转回首页
    useEffect(() => {
        if (isAuth) {
            navigate('/');
            return
        }
    }, [isAuth, navigate]);
    const onFinish = (loginData: UserAuthentication) => {
        setLoginButtonLoading(true)
        loginData.accountType = AccountType.PASSWORD
        dispatch(fetchLogin(loginData)).then(async () => {
            setLoginButtonLoading(false)
            const fromRouter = location.state?.from;
            const from = (fromRouter && fromRouter.endsWith('login')) ? '/' : fromRouter || '/';
            router.routes[0]?.children!.push(...parseRouters(await dispatch(fetchUserMenus())))
            router.navigate(from).then()
        }).catch((err: Error) => {
            message.error(err.message).then()
        }).finally(() => {
            setTimeout(() => {
                setLoginButtonLoading(false);
            }, 1500)
        })

    };

    return (
        <>
            <div className="container">
                <div className="login-container">
                    <Button
                        className='switch-language-button'
                        onClick={async () => {
                            const switchLanguage: string = language === 'zh' ? 'en' : 'zh'
                            setLanguage(switchLanguage);
                            dispatch(changeLanguage(switchLanguage));
                            await i18n.changeLanguage(switchLanguage).then();
                        }}
                        icon={
                            <IconFont type={language === 'zh' ? 'i-en' : 'i-cn'}/>
                        }/>
                    <Form className="login-form"
                          name="login"
                          initialValues={{remember: true}}
                          onFinish={onFinish}
                    >
                        <h3 className="title">{t('Header.title')}</h3>
                        <Form.Item
                            name="identify"
                            rules={[{required: true, message: t('Login.usernameEmptyMessage')}]}
                        >
                            <Input prefix={<UserOutlined/>} placeholder={t('Login.usernamePlaceholder')}/>
                        </Form.Item>
                        <Form.Item
                            name="credentials"
                            rules={[{required: true, message: t('Login.passwordEmptyMessage')}]}
                        >
                            <Input prefix={<LockOutlined/>} type="password"
                                   placeholder={t('Login.passwordPlaceholder')}/>
                        </Form.Item>
                        {/*<Form.Item name="remember" valuePropName="checked">*/}
                        {/*    <Checkbox>同意用户协议</Checkbox>*/}
                        {/*</Form.Item>*/}
                        <Divider className='other-login-divider'>
                            <p>{t('Login.otherLogin')}</p>
                        </Divider>
                        <div className='other-login-methods'>
                            {/*微信扫码*/}
                            <div className='other-login-method'>
                                <IconFont type="i-weixin"/>
                                <p>{t('Login.wechatLogin')}</p>
                            </div>
                            {/*weLink登录*/}
                            <div className='other-login-method'>
                                <IconFont type="i-WeLink"/>
                                <p>{t('Login.weLinkLogin')}</p>
                            </div>
                        </div>
                        <Form.Item>
                            <Button block type="primary" htmlType="submit" loading={loginButtonLoading}>
                                {t('Login.loginButton')}
                            </Button>
                        </Form.Item>
                    </Form>
                </div>
            </div>
        </>
    )
}
