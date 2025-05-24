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


import {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {Button, Flex, Form, Input, message} from "antd";
import {useUserStore} from "@/store";
import {IUserinfo} from "@/types/user";
import {usePageTitle} from "@/hooks/usePageTitle.tsx";

export const Userinfo = () => {
    usePageTitle('Menu.profile');
    const {t} = useTranslation();
    const {userinfo, fetchEditUserinfo} = useUserStore(state => state);
    const [form] = Form.useForm();

    const [buttonLoading, setButtonLoading] = useState<boolean>(false);

    useEffect(() => {
        form.setFieldsValue({...userinfo});
    }, [form, userinfo])


  const onFinish = async (values: IUserinfo) => {
        setButtonLoading(true);
        try {
          const updateValues = {...userinfo, ...values}
            await fetchEditUserinfo(updateValues);
            form.setFieldsValue({...updateValues});
            setButtonLoading(false);
            await message.success(t('Common.success'))
        } catch {
            setButtonLoading(false);
            await message.error(t('Common.failed'));
        }
    }


    return (
        <>


            <Flex
                className={'userinfo-form'}
                justify={'space-between'}>
              <Form<IUserinfo>
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
            </Flex>
        </>
    )
}
