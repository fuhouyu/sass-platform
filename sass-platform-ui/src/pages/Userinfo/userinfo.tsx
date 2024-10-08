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
import {useAppSelector} from "@/store";
import {UserinfoInterface} from "@/model/user";
import {Button, Form, Input, Radio} from "antd";
import './index.scss'

interface UserinfoFormInterface {
    label: string;
    key: string;
    value: string;
    disabled: boolean;
}

/**
 * 用户详情
 * @constructor 构造函数
 */
export const Userinfo: React.FC = () => {

    const userinfo: UserinfoInterface = useAppSelector<UserinfoInterface>((state: {
        user: { userinfo: UserinfoInterface }
    }) => state.user.userinfo)


    const formItem: UserinfoFormInterface[] = [
        {key: 'username', label: '登录名', value: userinfo.username, disabled: true},
        {key: 'realName', label: '真实姓名', value: userinfo.realName, disabled: false},
        {key: 'nickname', label: '昵称', value: userinfo.nickname, disabled: false},
        {key: 'email', label: '邮箱', value: userinfo.email, disabled: false},
        {key: 'loginDate', label: '最后登录时间', value: userinfo.loginDate, disabled: true},
        {key: 'loginIp', label: '最后登录ip', value: userinfo.loginIp, disabled: true},
    ]

    const [formValues, setFormValues] = useState<UserinfoFormInterface[]>(formItem);

    useEffect(() => {

    }, [formValues])

    const handleInputClick = (item: UserinfoFormInterface) => {
        item.value = '';
        setFormValues(formValues);
    }

    return (
        <>
            <Form className="userinfo-form"
                  name="basic"
                  labelCol={{span: 8}}
                  wrapperCol={{span: 16}}
                  style={{maxWidth: 600}}
                // initialValues={{remember: true}}
                // onFinish={onFinish}
                // onFinishFailed={onFinishFailed}
                  autoComplete="off"
            >
                {formValues.map((item: UserinfoFormInterface,) => (
                    <Form.Item<UserinfoInterface>
                        label={item.label}
                        name={item.key}
                        key={item.key}
                        initialValue={item.value}
                    >
                        <Input disabled={item.disabled} key={item.key}
                               onClick={() => handleInputClick(item)}/>
                    </Form.Item>
                ))}

                <Form.Item name="gender" key="gender" label="性别" initialValue={userinfo.gender}>
                    <Radio.Group value={userinfo.gender}>
                        <Radio value='male'>男</Radio>
                        <Radio value='female'>女</Radio>
                    </Radio.Group>
                </Form.Item>
                <div className="userinfo-submit text-align-center">
                    <Button type="primary" htmlType="submit">
                        保存
                    </Button>
                    <Button type="primary" htmlType="submit" danger>
                        取消
                    </Button>
                </div>
            </Form>
        </>
    );
}
