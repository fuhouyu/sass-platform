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

import {Button, Col, Drawer, Flex, Form, Input, InputNumber, message, Radio, Row, Select, Space, Tooltip} from "antd"
import {Trans, useTranslation} from "react-i18next";
import {Application as ApplicationModel} from "@/model/application.tsx";
import {CommonConstant} from "@/constants/commonConstant.tsx";
import {applicationApi} from "@/apis/application.tsx";
import {IconFont, S3Upload} from "@/components";
import TextArea from "antd/es/input/TextArea";
import {AnyObject} from "antd/es/_util/type";
import React, {useCallback, useEffect, useRef, useState} from "react";
import {useResourceAction} from "@/hooks/useResourceAction.tsx";
import {useLocaleStore} from "@/store";
import {useDictItem} from "@/hooks/useDictItem.tsx";
import {UploadFileStatus} from "antd/lib/upload/interface";
import {TableRefType} from "@components/List/table/interface.tsx";


export interface ApplicationManageFormProps {
    /**
     * 是否打开
     */
    isOpen: boolean;
    /**
     * updateId
     */
    updateId: string | undefined;
    /**
     * onCLose
     */
    onClose: () => void


}

const initForm: ApplicationModel = {
    isEnabled: true,
    published: true,
}

export const ApplicationManageForm = (props: ApplicationManageFormProps) => {
    const {t} = useTranslation();
    const {updateId} = props;
    const {preview} = useResourceAction();
    const [form] = Form.useForm();
    const {findDictItems} = useDictItem(["GRANT_TYPE"]);
    const language = useLocaleStore((state) => state.language);
    const tableRef = useRef<TableRefType<ApplicationModel>>(null);
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);

    const [iconFiles, setIconFiles] = useState<{
        uid: string,
        name: string,
        status?: UploadFileStatus,
        url?: string
    }[] | undefined>([]);

    /**
     * 初始化应用
     */
    const initApplicationInfo = useCallback(async () => {
        if (!updateId) {
            return
        }
        const applicationInfo: ApplicationModel = await applicationApi.getInfoByIdApi(updateId);
        if (applicationInfo.icon) {
            const icon = applicationInfo.icon as unknown as string;
            setIconFiles([{
                uid: icon ?? '',
                name: 'icon.png',
                status: 'done',
                url: preview(icon),
            }]);
        }
        form.setFieldsValue({...applicationInfo});
    }, [updateId])

    useEffect(() => {
        initApplicationInfo().then()
    }, [initApplicationInfo]);

    /**
     * 处理表单
     */
    const handleForm = async () => {
        await form.validateFields();
        const application: ApplicationModel = form.getFieldsValue();
        setIsModalButtonLoading(true);
        try {
            application.icon = iconFiles![0].uid as unknown as number
            await (props.updateId ? applicationApi.editInfoApi(props.updateId, application) : applicationApi.saveInfoApi(application));
            message.success(t('Common.success')).then()
            await tableRef?.current?.refreshPageList();
            props.onClose();
        } finally {
            setIsModalButtonLoading(false)
            setIconFiles(undefined)
        }
    }


    return (
        <Drawer
            title={props.updateId === undefined ? t('Application.add') : t("Application.edit")}
            open={props.isOpen}
            width={'50%'}
            closable
            destroyOnClose
            footer={
                <Flex justify={'flex-end'}>
                    <Space>
                        <Button key='onOk' type="primary" loading={isModalButtonLoading}
                                onClick={handleForm}>{t('Button.submit')}</Button>
                        <Button key='onCancel' onClick={props.onClose}>{t('Button.cancel')}</Button>
                    </Space>
                </Flex>
            }
            onClose={() => {
                props.onClose();
                setIconFiles(undefined);
            }}
        >
            <Form<ApplicationModel>
                form={form}
                labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 7 : 9}}
                clearOnDestroy={true}
                autoComplete="off"
            >

                <Row gutter={24}>
                    <Col span={12}>
                        <Form.Item
                            label={t('Application.clientName')}
                            name="clientName"
                            validateTrigger="onBlur"
                            key="clientName"
                            colon={false}
                            required={true}
                            hasFeedback
                            validateFirst={true}
                            rules={[{
                                required: true,
                                type: "string",
                                message: t('Application.clientNamePlaceholder'),
                                max: 50,
                            }
                            ]}
                        >
                            <Input placeholder={t('Application.clientNamePlaceholder')} maxLength={50}/>
                        </Form.Item>

                        <Form.Item
                            label={t('Application.clientId')}
                            name="clientId"
                            validateTrigger="onBlur"
                            key="clientId"
                            colon={false}
                            required={true}
                            validateFirst={true}
                            hasFeedback
                            rules={[{
                                type: "string",
                                required: true,
                                message: t('Application.clientIdPlaceholder'),
                                max: 50,
                            },
                                {
                                    required: true,
                                    validator: async (_, clientId: string) => {
                                        if (props.updateId || clientId == null || clientId == '') {
                                            return;
                                        }
                                        const exists = await applicationApi.checkClientIdExists(clientId);
                                        if (exists) {
                                            return Promise.reject(new Error(t('Application.clientIdPlaceholder')));
                                        }
                                    }
                                }
                            ]}
                        >
                            <Input
                                disabled={props.updateId !== undefined}
                                suffix={<Tooltip title={t('Application.clientIdTips')}>
                                    <IconFont type={'i-tips-hint'}/>
                                </Tooltip>}
                                placeholder={t('Application.clientIdPlaceholder')}
                                maxLength={50}/>
                        </Form.Item>

                        <Form.Item
                            label={t('Application.clientSecret')}
                            name="clientSecret"
                            validateTrigger="onBlur"
                            key="clientSecret"
                            colon={false}
                            required={true}
                            validateFirst={true}
                            hasFeedback
                            rules={[{
                                required: true,
                                type: "string",
                                message: t('Application.clientSecretTips'),
                                max: 50,
                            }]}
                        >
                            <Space.Compact style={{width: '100%'}}>
                                <Form.Item
                                    name="clientSecret"
                                    noStyle
                                >
                                    <Input
                                        disabled
                                        maxLength={50}
                                        placeholder={t('Application.clientSecretTips')}
                                    />
                                </Form.Item>
                                <Button
                                    type="primary"
                                    onClick={async () => {
                                        const secretKey = await applicationApi.generateClientSecret();
                                        form.setFieldValue('clientSecret', secretKey);
                                    }}
                                >
                                    {t('Application.clientSecretGenerate')}
                                </Button>
                            </Space.Compact>

                        </Form.Item>


                        <Form.Item
                            label={t('Application.redirectUris')}
                            name="redirectUris"
                            validateTrigger="onBlur"
                            key="redirectUris"
                            colon={false}
                            required={true}
                            validateFirst={true}
                            hasFeedback
                            rules={[
                                {
                                    required: true,
                                    type: "array",
                                    message: t('Application.redirectUrisPlaceholder'),
                                },
                                {
                                    validator: async (_, value) => {
                                        if (!Array.isArray(value)) return Promise.resolve();
                                        const invalid = value.find(uri => !/^https?:\/\/.+/.test(uri));
                                        if (invalid) {
                                            return Promise.reject(
                                                new Error(t('Application.redirectUrisErrorMessage'))
                                            );
                                        }
                                        return Promise.resolve();
                                    }
                                }
                            ]}
                        >
                            <Select
                                filterOption={false}
                                suffixIcon={null}
                                mode="tags"
                                tokenSeparators={[';', ',']}
                                placeholder={t('Application.redirectUrisPlaceholder')}
                            />
                        </Form.Item>

                        <Form.Item
                            label={t('Application.grantTypes')}
                            name="grantTypes"
                            validateTrigger="onBlur"
                            key="grantTypes"
                            colon={false}
                            required={true}
                            validateFirst={true}
                            hasFeedback
                            rules={[
                                {
                                    required: true,
                                    type: "array",
                                    message: t('Application.grantTypesPlaceholder'),
                                },
                            ]}
                        >
                            <Select
                                mode="multiple"
                                filterOption={false}
                                placeholder={t('Application.grantTypesPlaceholder')}
                                options={findDictItems('GRANT_TYPE').map(dictItem => {
                                    return {
                                        value: dictItem.itemCode,
                                        label: dictItem.itemName
                                    }
                                })}
                            />
                        </Form.Item>

                        <Form.Item
                            label={t('Application.ipWhitelist')}
                            name="ipWhitelist"
                            validateTrigger="onBlur"
                            key="ipWhitelist"
                            colon={false}
                            required={false}
                            hasFeedback
                            rules={[
                                {
                                    validator: async (_, value: string[]) => {
                                        if (!value || value.length === 0) {
                                            return Promise.resolve(); // 可选字段，空值通过
                                        }

                                        if (value.length > 5) {
                                            return Promise.reject(
                                                new Error(t('Application.ipWhitelistLimitError')) // 例：最多只能设置5个IP
                                            );
                                        }

                                        const ipRegExp =
                                            /^(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d{2}|[1-9]?\d)){3}$/;

                                        const invalidIp = value.find((ip) => !ipRegExp.test(ip));
                                        if (invalidIp) {
                                            return Promise.reject(
                                                new Error(t('Application.ipWhitelistErrorMessage')) // 例：请输入合法的IP地址
                                            );
                                        }

                                        return Promise.resolve();
                                    },
                                },
                            ]}
                        >
                            <Select
                                mode="tags"
                                tokenSeparators={[',', ';', ' ']}
                                placeholder={t('Application.ipWhitelistPlaceholder')}
                            />
                        </Form.Item>


                        <Form.Item
                            label={t('Application.accessTokenValidity')}
                            name="accessTokenValidity"
                            key="accessTokenValidity"
                            colon={false}
                            rules={[
                                {
                                    required: true,
                                    message: t('Application.accessTokenValidityPlaceholder'), // 请输入有效期
                                },
                                {
                                    type: 'number',
                                    min: 1,
                                    max: 86400, // 例如限制最大 86400 秒（24 小时）
                                    message: t('Application.accessTokenValidityRangeError'),
                                },
                            ]}
                        >
                            <InputNumber
                                min={1}
                                max={86400}
                                style={{width: '100%'}}
                                placeholder={t('Application.accessTokenValidityPlaceholder')}
                            />
                        </Form.Item>

                        <Form.Item
                            label={t('Application.refreshTokenValidity')}
                            name="refreshTokenValidity"
                            key="refreshTokenValidity"
                            colon={false}
                            rules={[
                                {
                                    required: true,
                                    message: t('Application.refreshTokenValidityPlaceholder'),
                                },
                                {
                                    type: 'number',
                                    min: 1,
                                    max: 2592000, // 限制最多30天（单位：秒）
                                    message: t('Application.refreshTokenValidityRangeError'),
                                },
                            ]}
                        >
                            <InputNumber
                                min={1}
                                max={2592000}
                                style={{width: '100%'}}
                                placeholder={t('Application.refreshTokenValidityPlaceholder')}
                            />
                        </Form.Item>


                        <Form.Item
                            label={t('Application.published')}
                            name="published"
                            key="published"
                            colon={false}
                            required={true}
                        >
                            <Radio.Group>
                                <Radio value={true}>{t('Common.yes')}</Radio>
                                <Radio value={false}>{t('Common.no')}</Radio>
                            </Radio.Group>
                        </Form.Item>


                        <Form.Item
                            label={t('Common.status')}
                            name="isEnabled"
                            key="isEnabled"
                            colon={false}
                            required={true}
                        >
                            <Radio.Group>
                                <Radio value={true}>{t('Common.enabled')}</Radio>
                                <Radio value={false}>{t('Common.disabled')}</Radio>
                            </Radio.Group>
                        </Form.Item>

                        <Form.Item
                            label={t('Common.remark')}
                            name="remark"
                            key="remark"
                            colon={false}
                        >
                            <TextArea className="remark"
                                      style={{
                                          height: '10rem',
                                      }}
                                      placeholder={t('Common.remark')} showCount maxLength={500}/>
                        </Form.Item>

                    </Col>

                    <Col span={12}>
                        <Form.Item
                            label={t('Application.icon.label')}
                            validateTrigger="onBlur"
                            key="icon"
                            colon={false}
                            required={true}
                            hasFeedback
                            name='icon'
                            valuePropName={'fileList'}
                            getValueProps={(resourceId) => {
                                return {
                                    uid: resourceId,
                                    name: 'icon.png',
                                    status: 'done',
                                    url: preview(resourceId),
                                };
                            }}
                            extra={
                                <div className={'icon-extra'}>
                                    <div>
                                        <Trans
                                            i18nKey="Application.icon.tips.suffix"
                                            values={{types: '.jpg, .png, .jpeg'}}
                                            components={{strong: <span className="highlight"/>}}
                                        />
                                    </div>
                                    <div>
                                        <Trans
                                            i18nKey="Application.icon.tips.size"
                                            values={{size: '1MB'}}
                                            components={{strong: <span className="highlight"/>}}
                                        />
                                    </div>
                                    <div>
                                        <Trans
                                            i18nKey="Application.icon.tips.count"
                                            values={{count: 1}}
                                            components={{strong: <span className="highlight"/>}}
                                        />
                                    </div>
                                </div>
                            }
                            validateFirst={true}
                            rules={[{
                                required: true,
                                type: "object",
                                message: t('Application.iconPlaceholder'),
                                validator: async (_, resourceId: AnyObject) => {
                                    return resourceId === null || resourceId === undefined;
                                }
                            }]}
                        >
                            <S3Upload
                                onRemove={_ => setIconFiles(undefined)}
                                prefix={'application-icon'}
                                isPublic
                                fileList={iconFiles}
                                accept={'image/*'}
                                maxCount={1}
                                showUploadList
                                onUploadSuccess={async (resourceId) => {
                                    setIconFiles([{
                                        uid: resourceId,
                                        name: 'icon.png',
                                        status: 'done',
                                        url: preview(resourceId),
                                    }]);
                                }}
                                listType="picture-card">
                                {iconFiles && null}
                            </S3Upload>
                        </Form.Item>

                    </Col>
                </Row>


            </Form>
        </Drawer>
    )
}