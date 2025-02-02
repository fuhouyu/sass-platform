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
import {Button, Divider, Form, Input, message} from "antd";
import {useLocation, useNavigate} from "react-router-dom";
import {fetchLogin} from "@/store/modules/user";
import {useAppDispatch} from "@/store";
import {UserAuthentication} from "@/model/authentication";
import useAuth from "@/hooks/useAuth";
import {IconFont, WeLinkLogin} from "@/components";
import {useTranslation} from "react-i18next";
import {router} from "@/routes/routers";
import {BASE_PORTAL_URL} from "@/constants/commonConstant";
import useLanguageSwitcher from "@/hooks/useLanguageSwitcher";
import {AccountType} from "@/model/account.tsx";
import {Turnstile, TurnstileInstance} from "@marsidev/react-turnstile";

/**
 * 登录组件
 * @constructor
 */
export const Login: React.FC = () => {
    const navigate = useNavigate();
    const turnstileRef = useRef<TurnstileInstance | null>(null);
    const [loginButtonLoading, setLoginButtonLoading] = useState<boolean>(false);
    const location = useLocation();
    const dispatch = useAppDispatch();
    const isAuth = useAuth();
    const {LanguageSwitcherButton} = useLanguageSwitcher('switch-language-button');
    const {t} = useTranslation();
    const [weLinkQr, setWeLinkQr] = useState<boolean>(true);
    const [loginTitle, setLoginTitle] = useState<string>('weLinkLoginTitle');
    const [turnstileToken, setTurnstileToken] = useState<string | undefined>();
    // 如果本身存在token，跳转回首页
    useEffect(() => {
        if (isAuth) {
            navigate('/');
        }
    }, [isAuth, navigate]);
    const onFinish = (loginData: UserAuthentication) => {
        setLoginButtonLoading(true);
        loginData.accountType = AccountType.PASSWORD;
        loginData.cloudflareTurnstileToken = turnstileToken;
        dispatch(fetchLogin(loginData)).then(async () => {
            setLoginButtonLoading(false)
            router.navigate(BASE_PORTAL_URL, {state: location.state}).then();
        }).catch((err: Error) => {
            message.error(err.message).then();
            turnstileRef.current?.reset();
        }).finally(() => {
            setTimeout(() => {
                setLoginButtonLoading(false);
            }, 500)
        })
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
                              }}
                              onFinish={onFinish}
                        >
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
    )
}
