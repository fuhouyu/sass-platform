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

import React, {useEffect, useRef, useState} from "react";
import "./index.scss"
import {Avatar, Button, Divider, Form, Input, Select, Space} from "antd";
import {useLocation, useNavigate} from "react-router-dom";
import {UserAuthentication} from "@/model/authentication";
import useAuth from "@/hooks/useAuth";
import {IconFont, WeLinkLogin} from "@/components";
import {useTranslation} from "react-i18next";
import useLanguageSwitcher from "@/hooks/useLanguageSwitcher";
import {AccountType} from "@/model/account.tsx";
import {Turnstile, TurnstileInstance} from "@marsidev/react-turnstile";
import {useRouterStore, useUserStore} from "@/store";
import {BaseUrlConstant} from "@/constants/baseUrlConstant.tsx";
import {TenantInfo} from "@/model/tenant.tsx";
import {tenantApi} from "@/apis/tenant.tsx";
import {useResourceAction} from "@/hooks/useResourceAction.tsx";
import {parseRoutes} from "@/hooks/useRoutes.tsx";

/**
 * 登录组件
 * @constructor
 */
export const Login: React.FC = () => {
    const navigate = useNavigate();
    const turnstileRef = useRef<TurnstileInstance | null>(null);
    const [loginButtonLoading, setLoginButtonLoading] = useState<boolean>(false);
    const location = useLocation();
    const isAuth = useAuth();
    const {LanguageSwitcherButton} = useLanguageSwitcher('switch-language-button');
    const {t} = useTranslation();
    const [weLinkQr, setWeLinkQr] = useState<boolean>(true);
    const [loginTitle, setLoginTitle] = useState<string>('weLinkLoginTitle');
    const [turnstileToken, setTurnstileToken] = useState<string | undefined>();
    const {fetchLogin} = useUserStore(state => state);
    const {preview} = useResourceAction();
    const [tenantList, setTenantList] = useState<TenantInfo[]>([]);
    const {fetchUserMenus} = useUserStore(state => state);
    const router = useRouterStore(state => state.router);
    const initTenantList = async () => {
        setTenantList(await tenantApi.list());
    }
    // 如果本身存在token，跳转回首页
    useEffect(() => {
        if (isAuth) {
            navigate('/');
        }
        initTenantList().then();
    }, [isAuth, navigate]);
    const onFinish = async (loginData: UserAuthentication) => {
        setLoginButtonLoading(true);
        loginData.accountType = AccountType.PASSWORD;
        loginData.cloudflareTurnstileToken = turnstileToken;
        try {
            await fetchLogin(loginData);
            setLoginButtonLoading(false)
            setLoginButtonLoading(false);
            const fromRouter = location.state?.from;
            const from = (fromRouter && fromRouter.endsWith(BaseUrlConstant.LOGIN_URL)) ? '/' : fromRouter || '/';
            const menus = await fetchUserMenus();
            if (router?.routes[0]?.children) {
                router.routes[0].children.push(...parseRoutes(menus));
            }
            navigate(from);

        } finally {
            setLoginButtonLoading(false);
        }
    };


    return (
        <div className="container">
            <div className="login-container">
                {LanguageSwitcherButton}
                <h3 className="title" dangerouslySetInnerHTML={{__html: t(`Login.${loginTitle}`)}}/>
                {
                    weLinkQr ?
                        <WeLinkLogin redirectType={'login'}/>
                        :

                        <Form className="login-form"
                              name="login"
                              initialValues={{
                                  "identify": "admin",
                                  "credentials": "admin",
                                  "tenantId": tenantList[0].id
                              }}
                              onFinish={onFinish}
                        >
                            <Form.Item
                                name="tenantId"
                                required
                                rules={[
                                    {required: true, message: t('Login.tenantChoosePlaceholder')}
                                ]}
                            >
                                <Select
                                    prefix={<IconFont type={'i-zuhuguanli'}/>}
                                    className={'tenant-choose-container'}
                                    options={tenantList.map(tenantInfo => {
                                        return {
                                            value: tenantInfo.id,
                                            label: <Space className={'tenant-choose'}>
                                                <Avatar
                                                    icon={null}
                                                    src={preview(tenantInfo.icon)}/>{tenantInfo.tenantName}
                                            </Space>
                                        }
                                    })}/>
                            </Form.Item>
                            <Form.Item
                                name="identify"
                                rules={[{required: true, message: t('Login.usernameEmptyMessage')}]}
                            >
                                <Input prefix={<IconFont type={'i-zhanghao'}/>}
                                       placeholder={t('Login.usernamePlaceholder')}/>
                            </Form.Item>
                            <Form.Item
                                name="credentials"
                                rules={[{required: true, message: t('Login.passwordEmptyMessage')}]}
                            >
                                <Input.Password prefix={<IconFont type={'i-mima'}/>}

                                                placeholder={t('Login.passwordPlaceholder')}/>
                            </Form.Item>
                            {import.meta.env.VITE_CLOUDFLARE_SITE_KEY &&
                                <Form.Item className={'cloudflare-turnstile'}>
                                    <label>
                                        <span>{t('Login.cloudflareTurnstileVerify')}</span>
                                    </label>
                                    <Turnstile
                                        ref={turnstileRef}
                                        options={{
                                            theme: 'light',
                                            size: 'flexible',
                                        }}
                                        siteKey={import.meta.env.VITE_CLOUDFLARE_SITE_KEY}
                                        onSuccess={(token: string) => setTurnstileToken(token)}
                                    />
                                </Form.Item>
                            }

                            <Form.Item className={'login-button-container'}>
                                <Button disabled={import.meta.env.VITE_CLOUDFLARE_SITE_KEY && !turnstileToken} block
                                        type="primary" htmlType="submit" loading={loginButtonLoading}>
                                    {t('Login.loginButton')}
                                </Button>
                            </Form.Item>
                        </Form>
                }
                <Divider className='other-login-divider'>
                    <p>{t('Login.otherLogin')}</p>
                </Divider>
                <div className='other-login-methods'>
                    {weLinkQr ? <Button icon={<IconFont type="i-zhanghao"/>}
                                        color="default"
                                        variant="link"
                                        className='other-login-method'
                                        onClick={() => {
                                            setWeLinkQr(false);
                                            setLoginTitle('usernamePasswordLoginTitle');
                                        }}>
                            <p>{t('Login.usernamePasswordLogin')}</p>
                        </Button> :
                        <Button icon={<IconFont type="i-WeLink"/>}
                                color="default"
                                variant="link"
                                className='other-login-method'
                                onClick={() => {
                                    setWeLinkQr(true);
                                    setLoginTitle('weLinkLoginTitle');
                                }}>

                            <p>{t('Login.weLinkLogin')}</p>
                        </Button>
                    }


                </div>
            </div>

        </div>
    );
}
