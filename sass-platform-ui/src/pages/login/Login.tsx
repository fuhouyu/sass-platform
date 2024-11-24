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
import {Button, Checkbox, Col, Divider, Form, Input, message, Row} from "antd";
import {LockOutlined, UserOutlined} from '@ant-design/icons';
import {useLocation, useNavigate} from "react-router-dom";
import {fetchLogin, fetchUserMenus} from "@/store/modules/user";
import {useAppDispatch} from "@/store";
import {UserAuthenticationModel} from "@/model/authentication";
import useAuth from "@/hooks/useAuth";
import {AccountType} from "@/constants/accountTypeConstant";
import {IconFont} from "@/components";
import {parseRouters, router} from "@/routes/routers";
import {Menus} from "@/model/menus";


const Login: React.FC = () => {
    const navigate = useNavigate();
    const [loginButtonLoading, setLoginButtonLoading] = useState<boolean>(false);
    const location = useLocation();
    const dispatch = useAppDispatch();
    const isAuth = useAuth();

    // 如果本身存在token，跳转回首页
    useEffect(() => {

        if (isAuth) {
            router.navigate('/').then()
            return
        }
    }, [isAuth, navigate]);
    const onFinish = (loginData: UserAuthenticationModel) => {
        setLoginButtonLoading(true)
        loginData.accountType = AccountType.PASSWORD
        dispatch(fetchLogin(loginData)).then(() => {
            setLoginButtonLoading(false)
            const fromRouter = location.state?.from;
            const from = (fromRouter && fromRouter.endsWith('login')) ? '/' : fromRouter || '/';
            router.navigate(from)
                .then(() => {
                    // 设置权限
                    dispatch(fetchUserMenus())
                        .then((menuItems: Menus[]) => {
                            router.routes[0]?.children!.push(...parseRouters(menuItems))
                        })
                })
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
            <Row className="login-container" style={{height: '100vh'}}>
                {/* 左侧背景部分 */}
                <Col span={18} className="login-bg"/>

                {/* 右侧表单部分 */}
                <Col span={6} className="login-form-wrapper">
                    <Form className="login-form"
                          name="login"
                          initialValues={{remember: true}}
                          onFinish={onFinish}
                    >
                        <h3 className="title">Sass 后台管理系统</h3>
                        <Form.Item
                            name="identify"
                            rules={[{required: true, message: '请输入用户名!'}]}
                        >
                            <Input prefix={<UserOutlined/>} placeholder="请输入用户名"/>
                        </Form.Item>
                        <Form.Item
                            name="credentials"
                            rules={[{required: true, message: '请输入密码!'}]}
                        >
                            <Input prefix={<LockOutlined/>} type="password" placeholder="请输入密码"/>
                        </Form.Item>
                        <Form.Item name="remember" valuePropName="checked">
                            <Checkbox>同意用户协议</Checkbox>
                        </Form.Item>
                        <Divider style={{borderColor: '#7cb305'}}>
                            <IconFont type="i-wechat-fill"/>
                        </Divider>
                        <Form.Item>
                            <Button block type="primary" htmlType="submit" loading={loginButtonLoading}>
                                登录
                            </Button>
                        </Form.Item>
                    </Form>
                </Col>
            </Row>
        </>
    )
}

export default Login;