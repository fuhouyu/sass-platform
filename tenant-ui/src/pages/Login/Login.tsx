/*
 * Copyright 2024-2034 the original author or authors.
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
import {Button, Form, Input} from "antd";
import {LockOutlined, UserOutlined} from '@ant-design/icons';
import {useNavigate} from "react-router-dom";

const Login: React.FC = () => {
    const navigate = useNavigate();

    const onFinish = (values: string) => {
        console.log('Received values of form: ', values);
        // TODO 这里先随便写入一个
        localStorage.setItem("token", "token");
        navigate("/")

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
                        name="用户名"
                        rules={[{required: true, message: '请输入用户名!'}]}
                    >
                        <Input prefix={<UserOutlined/>} placeholder="请输入用户名"/>
                    </Form.Item>
                    <Form.Item
                        name="密码"
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