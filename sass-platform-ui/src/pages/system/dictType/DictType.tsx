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
import {DictType as DictTypeModel} from "@/model/dictType";
import {
    Button,
    Form,
    Input,
    InputNumber,
    message,
    Popconfirm,
    Radio,
    Select,
    Switch,
    TableColumnsType,
    Tooltip
} from "antd";
import {AddButton, DeleteButton, EditButton} from "@components/Button/commonButton";
import React, {useRef, useState} from "react";
import type {TableRowSelection} from "antd/es/table/interface";
import {IconFont, Modal, PageList, PermissionButton} from "@/components";
import {dictTypeApi} from '@/apis/dictType';
import TextArea from "antd/es/input/TextArea";
import {DictTypePermissionConstant} from "@/constants/permissionConstant.tsx";
import {useButton} from "@/hooks/useButton.tsx";
import {useLocaleStore} from "@/store";
import {CommonConstant} from "@/constants/commonConstant.tsx";
import useRouteSearchParams from "@/hooks/useRouteSearchParams.tsx";
import {TableRefType} from '@/components/List/table/interface';
import {Link} from 'react-router-dom';
import {usePageTitle} from "@/hooks/usePageTitle.tsx";

/**
 * 字典类型
 * @constructor 构造函数
 */
export const DictType = () => {
    usePageTitle('Menu.dictManage');
    const {t} = useTranslation();
    const buttonPermissions = useButton(DictTypePermissionConstant.List);
    const initForm: DictTypeModel = {
        displayOrder: 1,
        isEnabled: true,
    }
    const columns: TableColumnsType = [
        {
            title: t('DictType.name'),
            dataIndex: 'dictName',
            showSorterTooltip: {target: 'full-header'},
            align: "center",
        },
        {
            title: t('DictType.code'),
            dataIndex: 'dictCode',
            defaultSortOrder: 'descend',
            align: "center",
            render: (_, record: DictTypeModel) => {
                return <Link to={`/system/dict-item/${record.dictCode}`}>{record.dictCode}</Link>
            }
        },
        {
            title: t('Common.displayOrder'),
            dataIndex: 'displayOrder',
            align: "center",
            sorter: true,
            defaultSortOrder: "descend",
            showSorterTooltip: false
        },
        {
            title: t('Common.status'),
            dataIndex: 'isEnabled',
            align: 'center',
            render: (_, record: DictTypeModel) => (
                <Switch
                    disabled={!record.isAllowModified}
                    defaultChecked={record.isEnabled} onChange={async (checked) => {
                    await dictTypeApi.status(record.id!, checked);
                    await tableRef?.current?.refreshPageList();
                }}/>
            )
        },

        {
            title: t('Common.updatedAt'),
            dataIndex: 'updatedAt',
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
            render: (_, record: DictTypeModel) => {
                return (<>
                    <PermissionButton permissionStr={DictTypePermissionConstant.EDIT}
                                      buttonPermissions={buttonPermissions}>
                        <EditButton disabled={!record.isAllowModified} onClick={() => openModal(record.id)}/>
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
    const tableRef = useRef<TableRefType<DictTypeModel>>(null);
    const {querySearchParams, updateSearchParams} = useRouteSearchParams();
    const [dictTypeQuery, setDictTypeQuery] = useState<Record<string, string>>({...querySearchParams()});
    const [formInitValues, setFormInitValues] = useState<DictTypeModel>(initForm);
    const language = useLocaleStore((state) => state.language);
    /**
     * 打开模态组
     * @param dictTypeId 角色id
     */
    const openModal = async (dictTypeId?: string) => {
        setUpdateId(dictTypeId);
        if (dictTypeId) {
            const dictTypeInfo: DictTypeModel = await dictTypeApi.getInfoByIdApi(dictTypeId);
            setFormInitValues(dictTypeInfo);
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
        const dictType: DictTypeModel = form.getFieldsValue();
        setIsModalButtonLoading(true);
        try {
            await (updateId ? dictTypeApi.editInfoApi(updateId, dictType) : dictTypeApi.saveInfoApi(dictType));
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
    const rowSelection: TableRowSelection<DictTypeModel> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
        getCheckboxProps: (record: DictTypeModel) => ({
            disabled: !record.isAllowModified
        }),
    };

    return (
        <>
            <PageList
                tableProps={{
                    tableRef: tableRef,
                    tableName: t('DictType.list'),
                    columns: columns,
                    pageApi: dictTypeApi.pageInfoListApi,
                    rowSelection: rowSelection,
                    tableComponents: [
                        <>
                            <PermissionButton permissionStr={DictTypePermissionConstant.ADD}
                                              buttonPermissions={buttonPermissions}>
                                <AddButton onClick={() => openModal()}/>
                            </PermissionButton>
                            <PermissionButton permissionStr={DictTypePermissionConstant.DELETE}
                                              buttonPermissions={buttonPermissions}>
                                <Popconfirm
                                    title={t('Button.delete')}
                                    description={t('Button.deleteConfirm')}
                                    okText={t('Common.yes')}
                                    cancelText={t('Common.no')}
                                    onConfirm={async () => {
                                        dictTypeApi.deleteInfoApi(rowKeys as string[]).then();
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
                        <><label htmlFor="dictTypeCode">{t('DictType.code')}</label>
                            <Input
                                allowClear
                                defaultValue={dictTypeQuery.dictTypeCode}
                                placeholder={t('DictType.codePlaceholder')}
                                id={'dictTypeCode'}
                                onChange={(e) => {
                                    setDictTypeQuery({dictTypeCode: e.target.value})
                                }}/>
                        </>,
                        <>
                            <span>{t('Common.status')}</span>
                            <Select
                                allowClear
                                defaultValue={dictTypeQuery.isEnabled}
                                key={'isEnabled'}
                                placeholder={t('Common.statusPlaceholder')}
                                onChange={(value) => dictTypeQuery['isEnabled'] = value}
                                options={[
                                    {value: 'true', label: <span>{t('Common.enabled')}</span>},
                                    {value: 'false', label: <span>{t('Common.disabled')}</span>}
                                ]}
                            />
                        </>
                    ],
                    onSearchClick: () => updateSearchParams(dictTypeQuery)
                }}
            />

            <Modal
                centered
                destroyOnClose={true}
                title={updateId ? t('DictType.edit') : t('DictType.add')}
                open={isModalOpen}
                onCancel={() => setIsModalOpen(false)}
                footer={[
                    <Button key='onOk' type="primary" loading={isModalButtonLoading}
                            onClick={handleForm}>{t('Button.submit')}</Button>,
                    <Button key='onCancel' onClick={() => setIsModalOpen(false)}>{t('Button.cancel')}</Button>
                ]}
            >
                <Form<DictTypeModel>
                    name="modal-form"
                    form={form}
                    labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 4 : 7}}
                    clearOnDestroy={true}
                    autoComplete="off"
                    initialValues={{...formInitValues}}
                >
                    <Form.Item
                        label={t('DictType.name')}
                        name="dictName"
                        validateTrigger="onBlur"
                        key="dictName"
                        colon={false}
                        required={true}
                        hasFeedback
                        validateFirst={true}
                        rules={[{
                            required: true,
                            type: "string",
                            message: t('DictType.namePlaceholder'),
                            max: 50,
                        }
                        ]}
                    >
                        <Input placeholder={t('DictType.namePlaceholder')} maxLength={50}/>
                    </Form.Item>

                    <Form.Item
                        label={t('DictType.code')}
                        name="dictCode"
                        validateTrigger="onBlur"
                        key="dictCode"
                        colon={false}
                        required={true}
                        validateFirst={true}
                        hasFeedback
                        rules={[{
                            required: true,
                            type: "string",
                            message: t('DictType.codePlaceholder'),
                            max: 50,
                        },
                            {
                                required: true,
                                validator: async (_, value: string) => {
                                    if (updateId != null || value == null || value == '') {
                                        return;
                                    }
                                    const exists = await dictTypeApi.checkDictCode(value);
                                    if (exists) {
                                        return Promise.reject(new Error(t('DictType.codeExistsErrorMessage')));
                                    }
                                }
                            }
                        ]}
                    >
                        <Input
                            suffix={<Tooltip title={t('DictType.codeTips')}>
                                <IconFont type={'i-tips-hint'}/>
                            </Tooltip>}
                            disabled={updateId != null}
                            placeholder={t('DictType.codePlaceholder')}
                            maxLength={50}/>
                    </Form.Item>

                    <Form.Item
                        label={t('Common.displayOrder')}
                        name="displayOrder"
                        validateTrigger="onBlur"
                        key="displayOrder"
                        colon={false}
                        required={true}
                        hasFeedback
                        validateFirst={true}
                        rules={[{
                            required: true,
                            type: "number",
                            message: t('Common.displayOrderPlaceholder'),
                        }]}
                    >
                        <InputNumber placeholder={t('Common.displayOrderPlaceholder')} style={{width: '30%'}}
                        />
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