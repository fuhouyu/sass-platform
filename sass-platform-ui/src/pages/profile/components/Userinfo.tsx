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


import React, {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {Button, Flex, Form, Input, message} from "antd";
import {useUserStore} from "@/store";
import {Userinfo as UserinfoModal} from "@/model/user.tsx";

export const Userinfo = () => {

    const {t} = useTranslation();
    const {userinfo, fetchEditUserinfo} = useUserStore(state => state);
    const [form] = Form.useForm();

    const [buttonLoading, setButtonLoading] = useState<boolean>(false);

    useEffect(() => {
        form.setFieldsValue({...userinfo});
    }, [form, userinfo])


    const onFinish = async (values: UserinfoModal) => {
        setButtonLoading(true);
        try {
            const updateValues = Object.assign({}, userinfo, values);
            await fetchEditUserinfo(updateValues);
            form.setFieldsValue({...updateValues});
            setButtonLoading(false);
            await message.success('修改成功')
        } catch {
            setButtonLoading(false);
            await message.error('用户修改失败');
        }
    }


    return (
        <>


            <Flex
                className={'userinfo-form'}
                justify={'space-between'}>
                <Form<UserinfoModal>
                    form={form}
                    onFinish={onFinish}
                    layout="vertical"
                    style={{width: '80%'}}
                    wrapperCol={{span: 10}}
                    clearOnDestroy={true}
                    initialValues={{...userinfo}}
                >
                    <Form.Item
                        name={'realName'}
                        key={'realName'}
                        label={t('User.realName')}>
                        <Input key={'realName'} allowClear/>
                    </Form.Item>
                    <Form.Item
                        name={'nickname'}
                        key={'nickname'}
                        label={t('User.nickname')}>
                        <Input key={'nickname'} allowClear/>
                    </Form.Item>
                    <Form.Item
                        name={'email'}
                        key={'email'}
                        label={t('User.email')}>
                        <Input allowClear/>
                    </Form.Item>
                    <Form.Item>
                        <Button htmlType="submit" type="primary"
                                loading={buttonLoading}>{t('User.updateUserinfo')}</Button>
                    </Form.Item>
                </Form>

                {/*<div>*/}
                {/*    <Flex*/}
                {/*        vertical*/}
                {/*        align={'center'}*/}
                {/*        gap={10}*/}
                {/*    >*/}
                {/*        <Avatar*/}
                {/*            size={{xs: 100, sm: 100, md: 100, lg: 100, xl: 100, xxl: 100}}*/}
                {/*            src={"https://oss.fuhouyu.com/2.jpeg"}*/}
                {/*        />*/}
                {/*        <Upload>*/}
                {/*            <Button icon={<UploadOutlined/>}>{t('Common.updateAvatar')}</Button>*/}
                {/*        </Upload>*/}
                {/*    </Flex>*/}

                {/*</div>*/}
            </Flex>
        </>
    )
}