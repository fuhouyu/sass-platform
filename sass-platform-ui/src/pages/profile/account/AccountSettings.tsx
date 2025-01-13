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
import './index.scss'
import {Button, Card, Flex, Form, Input, Modal as AntdModal, Space} from "antd";
import {IconFont, Modal} from "@/components";
import React, {useEffect, useState} from "react";
import {accountApi} from "@/apis/account.tsx";
import {Account, AccountType} from "@/model/account.tsx";
import {useTranslation} from "react-i18next";
import {ExclamationCircleFilled} from "@ant-design/icons";
import {useAppSelector} from "@/store";

interface UpdatePasswordForm {
    oldPassword: string;
    newPassword: string;
    confirmPassword: string;
}

/**
 * 账号设置
 * @constructor 构造函数
 */
export const AccountSettings = () => {

    const [accounts, setAccounts] = useState<Account[]>([]);
    const weLinkBind = accounts.find(account => account.accountType === AccountType.WELINK);
    const [openModal, setOpenModal] = useState(false);
    const [isModalButtonLoading, setIsModalButtonLoading] = useState(false);
    const [passwordForm] = Form.useForm<UpdatePasswordForm>();
    const {t} = useTranslation()
    const language = useAppSelector(state => state.locale.language);
    const {confirm} = AntdModal;
    const getAccounts = async () => {
        setAccounts(await accountApi.getAccountForMe());
    }
    useEffect(() => {
        getAccounts().then();
    }, []);

    /**
     * 显示取消绑定的表单
     */
    const showUnbindConfirm = () => {
        confirm({
            title: t('Account.unbind'),
            icon: <ExclamationCircleFilled/>,
            content: t('Account.unbindAccountConfirm'),
            onOk() {
                unbind().then();
            },
            closable: true,
            destroyOnClose: true
        });
    };

    /**
     * 取消绑定账号
     */
    const unbind = async () => {
        await accountApi.unbindThirdPartyAccount(AccountType.WELINK, weLinkBind!.account);
        await getAccounts();
    }

    /**
     * 绑定账号
     */
    const bindAccount = () => {
        const width = 400; // 弹窗宽度
        const height = 500; // 弹窗高度
        const left = (window.screen.width - width) / 2; // 居中定位
        const top = (window.screen.height - height) / 2; // 居中定位
        const specs = `width=${width},height=${height},left=${left},top=${top},resizable=no,scrollbars=no`;

        const newWindow = window.open('/account-bind', '_blank', specs);

        // 定时检查窗口是否关闭
        const timer = setInterval(() => {
            if (newWindow?.closed) {
                clearInterval(timer);
                // 在这里处理窗口关闭后的逻辑，比如刷新页面或更新状态
                getAccounts().then();
            }
        }, 500);
    }


    /**
     * 修改密码
     */
    const updatePassword = async () => {
        await passwordForm.validateFields();
        setIsModalButtonLoading(true);
        const values = passwordForm.getFieldsValue();
        try {
            await accountApi.updatePasswordMe(values);
            setOpenModal(false);
        } finally {
            setIsModalButtonLoading(false);
        }
    }


    return (
        <div className={'account-container'}>
            <Card title={t('Account.personal')} bordered={false}>
                <ul className={'account-settings'}>
                    <li>
                        <Space>
                            <p>{t('Account.loginPassword')}</p>


                            <Input.Password
                                prefix={<IconFont type={'i-mima'}/>}
                                value={'******'} disabled/>
                            <Button onClick={() => setOpenModal(true)}>{t('Account.updatePassword')}</Button>
                        </Space>
                    </li>
                </ul>
            </Card>
            <Card title={t('Account.thirdPartyAccount')} bordered={false}>
                <ul>
                    <li>
                        <Flex justify={'space-between'} align={'center'}>
                            <div className={'account-left'}>
                                <IconFont type={'i-WeLink'} className={'account-icon'}/>
                                <div className={'text-block'}>
                                    <span className={'account-title'}>{t('Account.welink')}</span>
                                    {weLinkBind && <span
                                        className={'sub-title'}>{t('Account.alreadyBind')}：{weLinkBind.account}</span>}
                                </div>
                            </div>
                            <Button onClick={() =>
                                weLinkBind ? showUnbindConfirm() :
                                    bindAccount()
                            }
                                    icon={<IconFont type={weLinkBind ? 'i-jiechubangding' : 'i-bangdingpingtai'}/>}>
                                {weLinkBind ? t('Account.unbind') : t('Account.bind')}
                            </Button>
                        </Flex>
                    </li>
                </ul>
            </Card>

            <Modal
                title={t('Account.updatePassword')}
                open={openModal}
                onCancel={() => setOpenModal(false)}
                footer={[
                    <Button key='onOk' type="primary" loading={isModalButtonLoading}
                            onClick={updatePassword}>{t('Button.submit')}</Button>,
                    <Button key='onCancel' onClick={() => setOpenModal(false)}>{t('Button.cancel')}</Button>
                ]}
                closeIcon={<IconFont type="i-Close" style={{
                    fontSize: '1.5rem',
                }}/>}
                destroyOnClose={true}
            >
                <Form
                    form={passwordForm}
                    name="modal-form"
                    labelAlign={'right'}
                    labelCol={{span: language === 'zh' ? 4 : 7}}
                    colon={false}
                    clearOnDestroy={true}

                >

                    <Form.Item<UpdatePasswordForm>
                        label={t('Account.oldPassword')}
                        name="oldPassword"
                        rules={[{required: true, message: t('Account.oldPasswordPlaceholder')}]}
                    >
                        <Input.Password placeholder={t('Account.oldPasswordPlaceholder')}/>
                    </Form.Item>
                    <Form.Item<UpdatePasswordForm>
                        label={t('Account.newPassword')}
                        name="newPassword"
                        rules={[{required: true, message: t('Account.newPasswordPlaceholder')}]}
                    >
                        <Input.Password placeholder={t('Account.newPasswordPlaceholder')}/>
                    </Form.Item>

                    <Form.Item<UpdatePasswordForm>
                        label={t('Account.confirmPassword')}
                        name="confirmPassword"
                        rules={[{required: true, message: t('Account.confirmPasswordPlaceholder')}]}
                    >
                        <Input.Password placeholder={t('Account.confirmPasswordPlaceholder')}/>
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    );
};