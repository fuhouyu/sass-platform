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

import React, {FC, useRef, useState} from "react";
import './index.scss';
import {Application as ApplicationModel} from "@/model/application";
import {Trans, useTranslation} from "react-i18next";
import {useButton} from "@/hooks/useButton.tsx";
import {ApplicationPermissionConstant} from "@/constants/permissionConstant.tsx";
import {
    Button,
    Col,
    Form,
    Input,
    InputNumber,
    message,
    Popconfirm,
    Radio,
    Row,
    Select,
    Switch,
    TableColumnsType,
    Tooltip
} from "antd";
import {IconFont, Modal, PageList, PermissionButton, S3Upload} from "@/components";
import {AddButton, DeleteButton, EditButton} from "@components/Button/commonButton.tsx";
import {TableRefType} from "@components/List/table/interface.tsx";
import useRouteSearchParams from "@/hooks/useRouteSearchParams.tsx";
import {useLocaleStore} from "@/store";
import type {TableRowSelection} from "antd/es/table/interface";
import {applicationApi} from "@/apis/application.tsx";
import {CommonConstant} from "@/constants/commonConstant";
import {useResourceAction} from "@/hooks/useResourceAction.tsx";
import {UploadFileStatus} from "antd/lib/upload/interface";

export const ApplicationManage: FC = () => {

    const {t} = useTranslation();
    const {preview} = useResourceAction()
    const buttonPermissions = useButton(ApplicationPermissionConstant.List);
    const initForm: ApplicationModel = {
        isEnabled: true,
        published: true,
    }
    const columns: TableColumnsType = [
        {
            title: t('Application.clientName'),
            dataIndex: 'clientName',
            showSorterTooltip: {target: 'full-header'},
            align: "center",
        },
        {
            title: t('Application.clientId'),
            dataIndex: 'clientId',
            align: "center",
        },
        {
            title: t('Common.status'),
            dataIndex: 'isEnabled',
            align: 'center',
            render: (_, record: ApplicationModel) => (
                <Switch defaultChecked={record.isEnabled} onChange={async (checked) => {
                    await applicationApi.status(record.clientId!, checked);
                    await tableRef?.current?.refreshPageList();
                }}/>
            )
        },

        {
            title: t('Common.updatedAt'),
            dataIndex: 'updatedAt',
            defaultSortOrder: 'descend',
            align: "center",
        },
        {
            title: t('Common.updatedBy'),
            dataIndex: 'updatedBy',
            align: "center",
        },
        {
            title: t('Common.action'),
            dataIndex: 'action',
            align: "center",
            render: (_, record: ApplicationModel) => {
                return (
                    <PermissionButton permissionStr={ApplicationPermissionConstant.EDIT}
                                      buttonPermissions={buttonPermissions}>
                        <EditButton key={'edit'} onClick={() => openModal(record.clientId)}/>
                    </PermissionButton>
                )
            }
        }
    ];

    const tableRef = useRef<TableRefType<ApplicationModel>>(null);
    const [updateId, setUpdatedId] = useState<string | undefined>();
    const {querySearchParams, updateSearchParams} = useRouteSearchParams();
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const [form] = Form.useForm();
    const [pageQuery, setPageQuery] = useState<Record<string, string>>({...querySearchParams()});
    const [formInitValues, setFormInitValues] = useState<ApplicationModel>(initForm);
    const language = useLocaleStore((state) => state.language);
    const [iconFiles, setIconFiles] = useState<{
        uid: string,
        name: string,
        status?: UploadFileStatus,
        url?: string
    }[] | undefined>([]);

    /**
     * 打开模态组
     * @param clientId 角色id
     */
    const openModal = async (clientId?: string) => {
        setUpdatedId(clientId);
        if (clientId) {
            const applicationInfo: ApplicationModel = await applicationApi.getInfoByIdApi(clientId);
            if (applicationInfo.icon) {
                const icon = applicationInfo.icon as unknown as string;
                setIconFiles([{
                    uid: icon ?? '',
                    name: 'icon.png',
                    status: 'done',
                    url: preview(icon),
                }]);
            }
            setFormInitValues({...applicationInfo});
        } else {
            setFormInitValues(initForm);
        }
        setIsModalOpen(true);
    }

    /**
     * 处理表单
     */
    const handleForm = async () => {
        await form.validateFields();
        const application: ApplicationModel = form.getFieldsValue();
        setIsModalButtonLoading(true);
        try {
            application.icon = iconFiles![0].uid as unknown as number
            await (updateId ? applicationApi.editInfoApi(updateId, application) : applicationApi.saveInfoApi(application));
            message.success(t('Common.success')).then()
            await tableRef?.current?.refreshPageList();
            setIsModalOpen(false);
        } finally {
            setIsModalButtonLoading(false)
            setIconFiles(undefined)
        }
    }


    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<ApplicationModel> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
    };

    return (
        <>
            <PageList
                tableProps={{
                    tableRef: tableRef,
                    tableName: t('Application.list'),
                    columns: columns,
                    rowKey: 'clientId',
                    pageApi: applicationApi.pageInfoListApi,
                    rowSelection: rowSelection,
                    tableComponents: [
                        <>
                            <PermissionButton key={'add'} permissionStr={ApplicationPermissionConstant.ADD}
                                              buttonPermissions={buttonPermissions}>
                                <AddButton onClick={() => openModal()}/>
                            </PermissionButton>
                            <PermissionButton key={'delete'} permissionStr={ApplicationPermissionConstant.DELETE}
                                              buttonPermissions={buttonPermissions}>
                                <Popconfirm
                                    title={t('Button.delete')}
                                    description={t('Button.deleteConfirm')}
                                    okText={t('Common.yes')}
                                    cancelText={t('Common.no')}
                                    onConfirm={async () => {
                                        await applicationApi.deleteInfoApi(rowKeys as string[]);
                                        await tableRef?.current?.refreshPageList();
                                    }}
                                >
                                    <DeleteButton
                                        disabled={rowKeys === undefined || rowKeys.length === 0}/>
                                </Popconfirm>
                            </PermissionButton>
                        </>
                    ]
                }}
                headerSearchProps={{
                    components: [
                        <><label htmlFor="clientName">{t('Application.clientName')}</label>
                            <Input
                                allowClear
                                defaultValue={pageQuery.clientName}
                                placeholder={t('Application.clientNamePlaceholder')}
                                id={'clientName'}
                                onChange={(e) => setPageQuery({clientName: e.target.value})}/>
                        </>,
                        <>
                            <span>{t('Common.status')}</span>
                            <Select
                                defaultValue={pageQuery.isEnabled}
                                allowClear
                                key={'isEnabled'}
                                placeholder={t('Common.statusPlaceholder')}
                                onChange={(value) => pageQuery['isEnabled'] = value}
                                options={[
                                    {value: 'true', label: <span>{t('Common.enabled')}</span>},
                                    {value: 'false', label: <span>{t('Common.disabled')}</span>}
                                ]}
                            />
                        </>
                    ],
                    onSearchClick: () => updateSearchParams(pageQuery),

                }}
            />

            <Modal
                destroyOnClose={true}
                title={updateId ? t('Application.edit') : t('Application.add')}
                open={isModalOpen}
                width={1000}
                onCancel={() => {
                    setIsModalOpen(false);
                    setIconFiles(undefined)
                }}
                footer={[
                    <Button key='onOk' type="primary" loading={isModalButtonLoading}
                            onClick={handleForm}>{t('Button.submit')}</Button>,
                    <Button key='onCancel' onClick={() => setIsModalOpen(false)}>{t('Button.cancel')}</Button>
                ]}
            >
                <Form<ApplicationModel>
                    name="modal-form"
                    form={form}

                    labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 7 : 9}}
                    clearOnDestroy={true}
                    autoComplete="off"
                    initialValues={{...formInitValues}}
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
                                            if (updateId != null || clientId == null || clientId == '') {
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
                                    disabled={updateId !== undefined}
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
                                    message: t('Application.clientSecretPlaceholder'),
                                    max: 50,
                                }]}
                            >
                                <Input
                                    placeholder={t('Application.clientIdPlaceholder')}
                                    maxLength={50}/>
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
                                    mode="tags"
                                    filterOption={false}
                                    placeholder={t('Application.grantTypesPlaceholder')}
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
                                        message: t('Application.accessTokenValidityRequired'), // 请输入有效期
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
                                        message: t('Application.refreshTokenValidityRequired'),
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
                                }]}
                            >
                                <S3Upload
                                    onRemove={_ => setIconFiles(undefined)}
                                    prefix={'application-icon'}
                                    isPublic
                                    defaultFileList={iconFiles && iconFiles}
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
            </Modal>
        </>
    )
}