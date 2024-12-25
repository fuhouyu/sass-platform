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
import {useLocation, useNavigate, useSearchParams} from "react-router-dom";
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
    const {LanguageSwitcherButton} = useLanguageSwitcher('switch-language-button');
    const {t} = useTranslation();
    const [weLinkQr, setWeLinkQr] = useState<boolean>(true);
    const [loginTitle, setLoginTitle] = useState<string>('weLinkLoginTitle');
    const [searchParams] = useSearchParams();
    // 如果本身存在token，跳转回首页
    useEffect(() => {
        if (isAuth) {
            navigate('/');
        }
    }, [isAuth, navigate]);

    useEffect(() => {
        const accountType = searchParams.get('accountType');
        if (!accountType) {
            return;
        }
        const code = searchParams.get('code');
        dispatch(fetchLogin({accountType: accountType as AccountType, identify: code as string})).then(async () => {
            router.navigate(BASE_PORTAL_URL, {state: location.state}).then();
        }).catch((err: Error) => {
            message.error(err.message).then()
        })
    }, [dispatch, location.state, searchParams]);

    const onFinish = (loginData: UserAuthentication) => {

        setLoginButtonLoading(true)
        loginData.accountType = AccountType.PASSWORD
        dispatch(fetchLogin(loginData)).then(async () => {
            setLoginButtonLoading(false)
            router.navigate(BASE_PORTAL_URL, {state: location.state}).then();
        }).catch((err: Error) => {
            message.error(err.message).then()
        }).finally(() => {
            setTimeout(() => {
                setLoginButtonLoading(false);
            }, 1000)
        })
    };


    return (
        <div className="container">
            <div className="login-container">
                {LanguageSwitcherButton}
                <h3 className="title" dangerouslySetInnerHTML={{__html: t(`Login.${loginTitle}`)}}/>
                {
                    weLinkQr ?
                        <WeLinkLogin redirectUrl={import.meta.env.VITE_LGOIN_REDIRECT_URI}/>
                        :

                    <Form className="login-form"
                          name="login"
                          initialValues={{remember: true}}
                          onFinish={onFinish}
                    >
                        <Form.Item
                            name="identify"
                            initialValue={'admin'}
                            rules={[{required: true, message: t('Login.usernameEmptyMessage')}]}
                        >
                            <Input prefix={<IconFont type={'i-zhanghao'}/>}
                                   placeholder={t('Login.usernamePlaceholder')}/>
                        </Form.Item>
                        <Form.Item
                            name="credentials"
                            initialValue={'admin'}
                            rules={[{required: true, message: t('Login.passwordEmptyMessage')}]}
                        >
                            <Input.Password prefix={<IconFont type={'i-mima'}/>}

                                   placeholder={t('Login.passwordPlaceholder')}/>
                        </Form.Item>

                        {/*<Form.Item name="remember" valuePropName="checked">*/}
                        {/*    <Checkbox>同意用户协议</Checkbox>*/}
                        {/*</Form.Item>*/}
                        <Form.Item className={'login-button-container'}>
                            <Button block type="primary" htmlType="submit" loading={loginButtonLoading}>
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
