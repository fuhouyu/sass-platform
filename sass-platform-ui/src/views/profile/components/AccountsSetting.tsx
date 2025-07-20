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
import './index.scss'
import {Button, Col, Descriptions, DescriptionsProps, Flex, Form, Input, Row, Space} from "antd";
import {Modal} from "@/components";
import {useState} from "react";
import {accountApi} from "@/apis/account.ts";
import {useTranslation} from "react-i18next";
import {EditOutlined} from "@ant-design/icons";
import {useLocaleStore, useUserStore} from "@/store";
import {useForm} from "antd/es/form/Form";
import {useNotification} from "@/hooks/useNotification.tsx";
import {usePageTitle} from "@/hooks/usePageTitle.tsx";
import {Passkey} from "@/views/passkey";

interface EditPasswordForm {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
}

/**
 * 账号设置
 * @constructor 构造函数
 */
export const AccountsSetting = () => {
  usePageTitle('Menu.accountsSetting');
  const [updatePasswordForm] = useForm<EditPasswordForm>();
  const {t} = useTranslation()
  const language = useLocaleStore((state) => state.language);
  const {userinfo} = useUserStore(state => state);
  const [passwordEditModalOpen, setPasswordEditModalOpen] = useState<boolean>(false);
  const {notificationMessage, contextHolder} = useNotification();


  // 用户信息
  const userinfoItems: DescriptionsProps['items'] = [
    {
      key: 'username',
      label: t('User.username'),
      children: <Row gutter={12}>
        <Col>{userinfo.username}</Col>
      </Row>,
    },
    {
      key: 'password',
      label: t('User.password'),
      children: <Row gutter={12}>
        <Col>******</Col>
        <Col>
          <Button
            icon={<EditOutlined/>}
            size={'small'}
            onClick={() => setPasswordEditModalOpen(true)}
            color={'primary'}
            variant={'outlined'}>{t('User.editPassword')}</Button>
        </Col>
      </Row>,
    },
  ];

  return (
    <>
      {contextHolder}
      <div className={'account-container'}>
        <Flex vertical className={'user-account-info'} align={'center'} justify={'center'}>
          <Space direction={'vertical'} size={'large'}>
            <Descriptions title="基本信息" items={userinfoItems}/>
            <Passkey/>
          </Space>
        </Flex>
        <Modal
          title={t('Account.updatePassword')}
          width={600}
          open={passwordEditModalOpen}
          closable
          destroyOnHidden
          onCancel={() => setPasswordEditModalOpen(false)}
          onOk={() => {
            updatePasswordForm.validateFields().then(async (values) => {
              await accountApi.updatePasswordMe({
                ...values
              });
              setPasswordEditModalOpen(false);
              notificationMessage({type: 'success', message: t('Account.updatePasswordSuccess')});
            });
          }}
        >
          <Form<EditPasswordForm>
            form={updatePasswordForm}
            name="updatePassword"
            labelCol={{span: language === 'zh_cn' ? 4 : 6}}
            clearOnDestroy={true}
            validateTrigger={'onBlur'}
            initialValues={{remember: true}}
          >

            <Form.Item<EditPasswordForm>
              label={t('Account.oldPassword')}
              name="oldPassword"
              rules={[{required: true, message: t('Account.oldPasswordPlaceholder')}]}
            >
              <Input.Password placeholder={t('Account.oldPasswordPlaceholder')}/>
            </Form.Item>
            <Form.Item<EditPasswordForm>
              label={t('Account.newPassword')}
              name="newPassword"
              rules={[{required: true, message: t('Account.newPasswordPlaceholder')}]}
            >
              <Input.Password placeholder={t('Account.newPasswordPlaceholder')}/>
            </Form.Item>

            <Form.Item<EditPasswordForm>
              label={t('Account.confirmPassword')}
              name="confirmPassword"
              rules={[
                {required: true, message: t('Account.confirmPasswordPlaceholder')},
                {
                  validator: async (_, confirmPassword: string) => {
                    if (confirmPassword !== updatePasswordForm.getFieldValue('newPassword')) {
                      return Promise.reject(new Error(t('Account.confirmPasswordCheckMessage')));
                    }
                  }
                }
              ]}
            >
              <Input.Password placeholder={t('Account.confirmPasswordPlaceholder')}/>
            </Form.Item>
          </Form>
        </Modal>
      </div>
    </>
  );
};
