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


import {useLocation, useSearchParams} from "react-router-dom";
import React, {useCallback, useEffect, useState} from "react";
import {AccountType} from "@/model/account.tsx";
import {router} from "@/routes/routers.tsx";
import {BASE_LOGIN_URL, BASE_PORTAL_URL, BASE_USER_PROFILE_URL} from "@/constants/commonConstant.tsx";
import {Button, Form, Input, message, Modal, Spin} from "antd";
import {accountApi} from "@/apis/account.tsx";
import {useTranslation} from "react-i18next";
import {IconFont} from "@/components";
import './index.scss';
import {ThirdPartyBindAuthentication, UserAuthentication} from "@/model/authentication.tsx";
import {useUserStore} from "@/store";

export const PostThirdPartyRedirect = () => {

    const [searchParams] = useSearchParams();
    const location = useLocation();
    const {t} = useTranslation();
    const [bindModal, setBindModal] = useState<boolean>(false);
    const [userForm] = Form.useForm<ThirdPartyBindAuthentication>();
    const [temporaryToken, setTemporaryToken] = useState<string>('');
    const {fetchLogin, fetchLoginAndBind} = useUserStore();

    /**
     * 用户登录
     */
    const login = useCallback((accountType: string, code: string) => {
        fetchLogin({accountType: accountType as AccountType, identify: code}).then(async (res) => {
            // 如果登录成功直接跳转
            if (!('userBindToken' in res)) {
                router.navigate(BASE_PORTAL_URL, {state: location.state}).then();
                return
            }
            setTemporaryToken(res.userBindToken);
            // 绑定账号
            setBindModal(true);

        }).catch((err) => {
            message.error(err.message).then()
            router.navigate(BASE_LOGIN_URL, {state: location.state}).then();
        });
    }, [location.state, fetchLogin]);

    /**
     * 登录表单
     */
    const loginForm = async () => {
        await userForm.validateFields();
        const userBindAuthentication: ThirdPartyBindAuthentication = userForm.getFieldsValue();
        userBindAuthentication.temporaryToken = temporaryToken;
        userBindAuthentication.accountType = AccountType.PASSWORD;
        try {
            await fetchLoginAndBind(userBindAuthentication);
        } catch {
            router.navigate(BASE_LOGIN_URL, {state: location.state}).then();
        }
        router.navigate(BASE_PORTAL_URL, {state: location.state}).then();
    }

    /**
     * 账号绑定
     * @param code 授权码
     * @param accountType 账号类型
     */
    const bindAccount = useCallback(async (accountType: string, code: string) => {
        await accountApi.bindThirdPartyAccount(accountType, code);
        router.navigate(BASE_USER_PROFILE_URL).then();
    }, []);


    useEffect(() => {
        const redirectType = searchParams.get('redirectType');
        const code = searchParams.get('code')!;
        const accountType = searchParams.get('accountType')!;
        if (!code || !accountType) {
            message.error(t('Common.paramsError')).then();
            router.navigate(BASE_LOGIN_URL).then();
            return;
        }
        if (redirectType == 'bind') {
            bindAccount(accountType, code).then(() => {
                message.success(t('Common.success')).then();
                window.close();
            });
        } else {
            login(accountType, code);
        }

    }, [bindAccount, login, searchParams, t]);


    return (
        <>
        <Spin delay={500} tip={t('Common.pending')} fullscreen={true} size="large" className="page-loading"/>
            <Modal
                title={t('Account.thirdPartyAccountBind')}
                open={bindModal}
                destroyOnClose
                footer={[]}
                width={600}
                closable
                onCancel={() => {
                    setBindModal(false);
                    router.navigate(BASE_LOGIN_URL).then();
                }}
                onClose={() => setBindModal(false)}
                className={'account-bind-modal'}
            >
                <div className={'account-icon'}>
                    <IconFont type={'i-WeLink'}/>
                </div>
                <p>{t('Account.loginAndBindTips')}</p>
                <Form
                    className={'account-bind-form'}
                    name="login"
                    style={{maxWidth: 600}}
                    clearOnDestroy={true}
                    form={userForm}
                    onFinish={loginForm}
                >
                    <Form.Item<UserAuthentication>
                        name="identify"
                        rules={[{required: true, message: t('Login.usernameEmptyMessage')}]}
                    >
                        <Input prefix={<IconFont type={'i-zhanghao'}/>}
                               placeholder={t('Login.usernamePlaceholder')}/>
                    </Form.Item>
                    <Form.Item<UserAuthentication>
                        name="credentials"
                        rules={[{required: true, message: t('Login.passwordEmptyMessage')}]}
                    >
                        <Input.Password prefix={<IconFont type={'i-mima'}/>}

                                        placeholder={t('Login.passwordPlaceholder')}/>
                    </Form.Item>
                    <Form.Item className={'login-button-container'}>
                        <Button block type="primary" htmlType="submit">
                            {t('Login.loginAndBindButton')}
                        </Button>
                    </Form.Item>
                    <Form.Item className={'account-tips'}>
                        <p>
                            {t('Account.bindAccountTips')}
                        </p>
                    </Form.Item>
                </Form>
            </Modal>
        </>
    )
};