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
import {useTranslation} from "react-i18next";
import {ParamConfig as ParamConfigModel} from "@/model/paramConfig";
import {Button, Form, Input, message, Popconfirm, TableColumnsType} from "antd";
import {AddButton, DeleteButton, EditButton} from "@components/Button/commonButton";
import {useRef, useState} from "react";
import type {TableRowSelection} from "antd/es/table/interface";
import {Modal, PageList, PermissionButton} from "@/components";
import {paramConfigApi} from '@/apis/paramConfig';
import TextArea from "antd/es/input/TextArea";
import {ParamConfigPermissionConstant} from "@/constants/permissionConstant.tsx";
import {useButton} from "@/hooks/useButton.tsx";
import {useLocaleStore} from "@/store";
import {CommonConstant} from "@/constants/commonConstant.tsx";
import useRouteSearchParams from "@/hooks/useRouteSearchParams.tsx";
import {TableRefType} from '@/components/List/table/interface';
import {usePageTitle} from "@/hooks/usePageTitle.tsx";

/**
 * 字典类型
 * @constructor 构造函数
 */
export const ParamConfig = () => {
    usePageTitle('Menu.paramConfig');
    const {t} = useTranslation();
    const buttonPermissions = useButton(ParamConfigPermissionConstant.List);
    const columns: TableColumnsType = [
        {
            title: t('ParamConfig.name'),
            dataIndex: 'configName',
            showSorterTooltip: {target: 'full-header'},
            align: "center",
        },
        {
            title: t('ParamConfig.key'),
            dataIndex: 'configKey',
            defaultSortOrder: 'descend',
            align: "center",
        },
        {
            title: t('ParamConfig.value'),
            dataIndex: 'configValue',
            defaultSortOrder: 'descend',
            align: "center",
        },
        {
            title: t('ParamConfig.groupKey'),
            dataIndex: 'groupKey',
            defaultSortOrder: 'descend',
            align: "center",
        },
        {
            title: t('Common.updatedAt'),
            dataIndex: 'updatedAt',
            width: 180,
            align: "center",
            showSorterTooltip: false
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
            render: (_, record: ParamConfigModel) => {
                return (<>
                    <PermissionButton permissionStr={ParamConfigPermissionConstant.EDIT}
                                      buttonPermissions={buttonPermissions}>
                        <EditButton onClick={() => openModal(record.id)}/>
                    </PermissionButton>
                </>)
            }
        }
    ];

    const [updateId, setUpdateId] = useState<string | undefined>();
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const [form] = Form.useForm();
    const tableRef = useRef<TableRefType<ParamConfigModel>>(null);
    const {querySearchParams, updateSearchParams} = useRouteSearchParams();
    const [pageQuery, setPageQuery] = useState<Record<string, string>>({...querySearchParams()});
    const [formInitValues, setFormInitValues] = useState<ParamConfigModel>({});
    const language = useLocaleStore((state) => state.language);
    /**
     * 打开模态组
     * @param id 角色id
     */
    const openModal = async (id?: string) => {
        setUpdateId(id);
        if (id) {
            const paramConfig: ParamConfigModel = await paramConfigApi.getInfoByIdApi(id);
            setFormInitValues(paramConfig);
        } else {
            setFormInitValues({});
        }
        setIsModalOpen(true);
    }

    /**
     * 处理表单
     */
    const handleForm = async () => {
        await form.validateFields();
        const paramConfig: ParamConfigModel = form.getFieldsValue();
        setIsModalButtonLoading(true);
        try {
            await (updateId ? paramConfigApi.editInfoApi(updateId, paramConfig) : paramConfigApi.saveInfoApi(paramConfig));
            message.success(t('Common.success')).then()
            await tableRef?.current?.refreshPageList();
            setIsModalOpen(false);
        } finally {
            setIsModalButtonLoading(false)
        }
    }


    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<ParamConfigModel> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
        getCheckboxProps: (record: ParamConfigModel) => ({
            disabled: !record.isAllowModified
        }),
    };

    return (
        <>
            <PageList
                tableProps={{
                    tableRef: tableRef,
                    tableName: t('ParamConfig.list'),
                    columns: columns,
                    pageApi: paramConfigApi.pageInfoListApi,
                    rowSelection: rowSelection,
                    tableComponents: [
                        <>
                            <PermissionButton permissionStr={ParamConfigPermissionConstant.ADD}
                                              buttonPermissions={buttonPermissions}>
                                <AddButton onClick={() => openModal()}/>
                            </PermissionButton>
                            <PermissionButton permissionStr={ParamConfigPermissionConstant.DELETE}
                                              buttonPermissions={buttonPermissions}>
                                <Popconfirm
                                    title={t('Button.delete')}
                                    description={t('Button.deleteConfirm')}
                                    okText={t('Common.yes')}
                                    cancelText={t('Common.no')}
                                    onConfirm={async () => {
                                        paramConfigApi.deleteInfoApi(rowKeys as string[]).then();
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
                        <><label htmlFor="configName">{t('ParamConfig.name')}</label>
                            <Input
                                allowClear
                                defaultValue={pageQuery.configName}
                                placeholder={t('ParamConfig.namePlaceholder')}
                                id={'configName'}
                                onChange={(e) => {
                                    setPageQuery({...pageQuery, configName: e.target.value})
                                }}/>
                        </>,
                        <><label htmlFor="configKey">{t('ParamConfig.key')}</label>
                            <Input
                                allowClear
                                defaultValue={pageQuery.configKey}
                                placeholder={t('ParamConfig.keyPlaceholder')}
                                id={'configName'}
                                onChange={(e) => {
                                    setPageQuery({...pageQuery, configKey: e.target.value})
                                }}/>
                        </>,
                    ],
                    onSearchClick: () => updateSearchParams(pageQuery)
                }}
            />

            <Modal
                centered
                destroyOnClose={true}
                title={updateId ? t('ParamConfig.edit') : t('ParamConfig.add')}
                open={isModalOpen}
                onCancel={() => setIsModalOpen(false)}
                footer={[
                    <Button key='onOk' type="primary" loading={isModalButtonLoading}
                            onClick={handleForm}>{t('Button.submit')}</Button>,
                    <Button key='onCancel' onClick={() => setIsModalOpen(false)}>{t('Button.cancel')}</Button>
                ]}
            >
                <Form<ParamConfigModel>
                    name="modal-form"
                    form={form}
                    labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 4 : 7}}
                    clearOnDestroy={true}
                    autoComplete="off"
                    initialValues={{...formInitValues}}
                >
                    <Form.Item
                        label={t('ParamConfig.name')}
                        name="configName"
                        validateTrigger="onBlur"
                        key="configName"
                        colon={false}
                        required={true}
                        hasFeedback
                        validateFirst={true}
                        rules={[{
                            required: true,
                            type: "string",
                            message: t('ParamConfig.namePlaceholder'),
                            max: 50,
                        }
                        ]}
                    >
                        <Input placeholder={t('ParamConfig.namePlaceholder')} maxLength={50}/>
                    </Form.Item>

                    <Form.Item
                        label={t('ParamConfig.key')}
                        name="configKey"
                        validateTrigger="onBlur"
                        key="configKey"
                        colon={false}
                        required={true}
                        validateFirst={true}
                        hasFeedback
                        rules={[{
                            required: true,
                            type: "string",
                            message: t('ParamConfig.keyPlaceholder'),
                            max: 50,
                        },
                            {
                                required: true,
                                validator: async (_, value: string) => {
                                    if (updateId != null || value == null || value == '') {
                                        return;
                                    }
                                    const exists = await paramConfigApi.checkConfigKeyExists(value);
                                    if (exists) {
                                        return Promise.reject(new Error(t('ParamConfig.keyExistsErrorMessage')));
                                    }
                                }
                            }
                        ]}
                    >
                        <Input
                            disabled={updateId != null}
                            placeholder={t('ParamConfig.keyPlaceholder')}
                            maxLength={50}/>
                    </Form.Item>

                    <Form.Item
                        label={t('ParamConfig.value')}
                        name="configValue"
                        validateTrigger="onBlur"
                        key="configValue"
                        colon={false}
                        required={true}
                        hasFeedback
                        validateFirst={true}
                        rules={[{
                            required: true,
                            type: "string",
                            message: t('ParamConfig.valuePlaceholder'),
                            max: 50,
                        },]}
                    >
                        <Input placeholder={t('ParamConfig.valuePlaceholder')}/>
                    </Form.Item>

                    <Form.Item
                        label={t('ParamConfig.groupKey')}
                        name="groupKey"
                        key="groupKeyd"
                        colon={false}
                        validateFirst={true}
                        required={true}
                        rules={[{
                            required: true,
                            type: "string",
                            message: t('ParamConfig.groupKeyPlaceholder'),
                        },]}
                    >
                        <Input
                            disabled={updateId != null}
                            placeholder={t('ParamConfig.groupKeyPlaceholder')}/>
                    </Form.Item>

                    <Form.Item
                        label={t('Common.remark')}
                        name="remark"
                        key="remark"
                        colon={false}
                        validateFirst={true}
                    >
                        <TextArea
                            className="remark"
                            placeholder={t('Common.remark')}
                            style={{height: 100}}
                            showCount maxLength={500}/>
                    </Form.Item>
                </Form>
            </Modal>
        </>
    )
}
