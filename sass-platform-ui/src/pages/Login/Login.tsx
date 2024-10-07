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

import React from "react";
import "./index.scss"
import {Button, Form, Input, message} from "antd";
import {LockOutlined, UserOutlined} from '@ant-design/icons';
import {useNavigate} from "react-router-dom";
import request from "@/utils/request/AxiosRequest";
import localStoreToken, {TokenInterface} from "@/utils/token/TokenUtil";


interface UserLogin {
    username: string;
    password: string;
    loginType: string;
}

const Login: React.FC = () => {
    const navigate = useNavigate();

    const onFinish = (loginData: UserLogin) => {
        loginData.loginType = 'password'
        request.post('v1/user/login', loginData, {
            headers: {
                'Authorization': 'Basic dGVzdDE6cGFzc3dvcmQ='
            },

        })
            .then((res: TokenInterface) => {
                localStoreToken.storeToken(res)
                navigate("/")
            }).catch((err: Error) => {
            message.error(err.message);
        })
    };

    return (
        <>
            <div className="form-container">
                <Form className="login-form"
                      name="login"
                      initialValues={{remember: true}}
                      onFinish={onFinish}
                >
                    <h3 className="login-title"> 多租户后台管理系统</h3>
                    <Form.Item
                        name="username"
                        rules={[{required: true, message: '请输入用户名!'}]}
                    >
                        <Input prefix={<UserOutlined/>} placeholder="请输入用户名"/>
                    </Form.Item>
                    <Form.Item
                        name="password"
                        rules={[{required: true, message: '请输入密码!'}]}
                    >
                        <Input prefix={<LockOutlined/>} type="password" placeholder="请输入密码"/>
                    </Form.Item>

                    <Form.Item>
                        <Button block type="primary" htmlType="submit">
                            登录
                        </Button>
                    </Form.Item>
                </Form>
            </div>
        </>
    )
}

export default Login;