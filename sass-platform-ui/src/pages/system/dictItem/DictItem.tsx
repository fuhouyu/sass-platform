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
import {DictItem as DictItemModel} from "@/model/dictItem";
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
import React, {useCallback, useEffect, useRef, useState} from "react";
import type {TableRowSelection} from "antd/es/table/interface";
import {IconFont, Modal, PageList, PermissionButton} from "@/components";
import TextArea from "antd/es/input/TextArea";
import {dictItemApi} from "@/apis/dictItem";
import {DictType} from "@/model/dictType";
import {useNavigate, useParams} from "react-router-dom";
import {useButton} from '@/hooks/useButton';
import {DictItemPermissionConstant} from "@/constants/permissionConstant.tsx";
import useRouteSearchParams from "@/hooks/useRouteSearchParams.tsx";
import {dictTypeApi} from '@/apis/dictType';
import {TableRefType} from '@/components/List/table/interface';
import {usePageTitle} from "@/hooks/usePageTitle.tsx";

/**
 * 字典项
 * @constructor 构造函数
 */
export const DictItem = () => {
    usePageTitle('Menu.dictItem');
    const {t} = useTranslation();
    const buttonPermissions = useButton(DictItemPermissionConstant.List);
    const columns: TableColumnsType = [
        {
            title: t('DictItem.name'),
            dataIndex: 'itemName',
            showSorterTooltip: {target: 'full-header'},
            align: "center",
        },
        {
            title: t('DictItem.code'),
            dataIndex: 'itemCode',
            defaultSortOrder: 'descend',
            align: "center",
        },
        {
            title: t('Common.displayOrder'),
            dataIndex: 'displayOrder',
            align: "center",
            sorter: true,
            defaultSortOrder: "descend",
        },
        {
            title: t('Common.status'),
            dataIndex: 'isEnabled',
            align: 'center',
            render: (_, record: DictItemModel) => (
                <Switch
                    disabled={!record.isAllowModified}
                    defaultChecked={record.isEnabled} onChange={async (checked) => {
                    await dictItemApi.status(record.id!, checked);
                    await tableRef?.current?.refreshPageList({
                        pageQuery: params
                    });
                }}/>
            )
        },

        {
            title: t('Common.updatedAt'),
            dataIndex: 'updatedAt',
            width: 180,
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
            render: (_, record: DictItemModel) => {
                return <PermissionButton permissionStr={DictItemPermissionConstant.EDIT}
                                         buttonPermissions={buttonPermissions}>
                    <EditButton disabled={!record.isAllowModified} onClick={() => openModal(record.id)}/>
                </PermissionButton>

            }
        }
    ];

    const [updateId, setUpdateId] = useState<string | undefined>();
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const [form] = Form.useForm();
    const [dictTypeList, setDictTypeList] = useState<DictType[]>([]);
    const params = useParams();
    const navigate = useNavigate();
    const [dictTypeCode, setDictTypeCode] = useState<string>(params.dictCode!);
    const tableRef = useRef<TableRefType<DictItemModel>>(null);
    const {querySearchParams} = useRouteSearchParams();
    const [dictItemQuery, setDictItemQuery] = useState<Record<string, string>>({...querySearchParams()});
    const initForm: DictItemModel = {
        displayOrder: 1,
        isEnabled: true,
        ...dictItemQuery,
        ...params
    }
    const [formInitValues, setFormInitValues] = useState<DictItemModel>(initForm);

    const queryDictType = useCallback(async () => {
        setDictTypeList(await dictTypeApi.getList());
    }, []);

    useEffect(() => {
        queryDictType().then();
    }, [])



    /**
     * 打开模态组
     * @param dictItemId 角色id
     */
    const openModal = async (dictItemId?: string) => {
        setUpdateId(dictItemId);
        if (dictItemId) {
            const dictItemInfo: DictItemModel = await dictItemApi.getInfoByIdApi(dictItemId);
            setFormInitValues(dictItemInfo);
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
        const dictItem: DictItemModel = form.getFieldsValue();
        setIsModalButtonLoading(true);
        try {
            await (updateId ? dictItemApi.editInfoApi(updateId, dictItem) : dictItemApi.saveInfoApi(dictItem));
            message.success(t('Common.success')).then()
            await tableRef?.current?.refreshPageList({
                pageQuery: params
            });
            setIsModalOpen(false);
        } finally {
            setIsModalButtonLoading(false)
        }
    }

    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<DictItemModel> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
        getCheckboxProps: (record: DictItemModel) => ({
            disabled: !record.isAllowModified
        }),
    };

    return (
        <>
            <PageList
                tableProps={{
                    tableRef: tableRef,
                    tableName: t('DictItem.list'),
                    columns: columns,
                    pageApi: (pageQuery) => {

                        return dictItemApi.pageInfoListApi({...pageQuery, ...params});
                    },
                    rowSelection: rowSelection,
                    tableComponents: [
                        <>
                            <PermissionButton permissionStr={DictItemPermissionConstant.ADD}
                                              buttonPermissions={buttonPermissions}>
                                <AddButton onClick={() => openModal()}/>
                            </PermissionButton>

                            <PermissionButton permissionStr={DictItemPermissionConstant.DELETE}
                                              buttonPermissions={buttonPermissions}>
                                <Popconfirm
                                    title={t('Button.delete')}
                                    description={t('Button.deleteConfirm')}
                                    okText={t('Common.yes')}
                                    cancelText={t('Common.no')}
                                    onConfirm={async () => {
                                        dictItemApi.deleteInfoApi(rowKeys as string[]).then();
                                        await tableRef?.current?.refreshPageList({
                                            pageQuery: params
                                        });
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
                        <>
                            <span>{t('DictType.name')}</span>
                            <Select
                                key={'dictCode'}
                                defaultValue={params.dictCode}
                                placeholder={t('DictType.namePlaceholder')}
                                onChange={(value) => setDictTypeCode(value)}
                                options={dictTypeList.map(dictItem => {
                                    return {value: dictItem.dictCode, label: <span>{dictItem.dictName}</span>}
                                })}
                            />
                        </>,
                        <><label htmlFor="dictItemCode">{t('DictItem.code')}</label>
                            <Input
                                allowClear
                                defaultValue={dictItemQuery.dictItemCode}
                                placeholder={t('DictItem.codePlaceholder')}
                                id={'dictItemCode'}
                                onChange={(e) => {
                                    setDictItemQuery({dictItemCode: e.target.value})
                                }}/>
                        </>,
                        <>
                            <span>{t('Common.status')}</span>
                            <Select
                                allowClear
                                defaultValue={dictItemQuery.isEnabled}
                                key={'isEnabled'}
                                placeholder={t('Common.statusPlaceholder')}
                                onChange={(value) => dictItemQuery['isEnabled'] = value}
                                options={[
                                    {value: 'true', label: <span>{t('Common.enabled')}</span>},
                                    {value: 'false', label: <span>{t('Common.disabled')}</span>}
                                ]}
                            />
                        </>
                    ],
                    onSearchClick: () => {
                        const queryString = new URLSearchParams(dictItemQuery).toString();
                        navigate(`/system/dict-item/${dictTypeCode}?${queryString}`);
                    }
                }}
            />

            <Modal
                centered
                destroyOnClose={true}
                title={updateId ? t('DictItem.edit') : t('DictItem.add')}
                open={isModalOpen}
                onCancel={() => setIsModalOpen(false)}
                footer={[
                    <Button key='onOk' type="primary" loading={isModalButtonLoading}
                            onClick={handleForm}>{t('Button.submit')}</Button>,
                    <Button key='onCancel' onClick={() => setIsModalOpen(false)}>{t('Button.cancel')}</Button>
                ]}
            >
                <Form<DictItemModel>
                    name="modal-form"
                    form={form}
                    labelCol={{span: 6}}
                    clearOnDestroy={true}
                    autoComplete="off"
                    initialValues={{...formInitValues}}
                >

                    <Form.Item
                        label={t('DictType.code')}
                        name="dictCode"
                        validateTrigger="onBlur"
                        key="dictCode"
                        colon={false}
                        required={true}
                        validateFirst={true}
                    >
                        <Input disabled/>
                    </Form.Item>

                    <Form.Item
                        label={t('DictItem.name')}
                        name="itemName"
                        validateTrigger="onBlur"
                        key="itemName"
                        colon={false}
                        required={true}
                        hasFeedback
                        validateFirst={true}
                        rules={[{
                            required: true,
                            type: "string",
                            message: t('DictItem.namePlaceholder'),
                            max: 50,
                        }
                        ]}
                    >
                        <Input placeholder={t('DictItem.namePlaceholder')} maxLength={50}/>
                    </Form.Item>

                    <Form.Item
                        label={t('DictItem.code')}
                        name="itemCode"
                        validateTrigger="onBlur"
                        key="itemCode"
                        colon={false}
                        required={true}
                        validateFirst={true}
                        hasFeedback
                        rules={updateId ? [] : [{
                            required: true,
                            type: "string",
                            message: t('DictItem.codePlaceholder'),
                            max: 50,
                        },
                            {
                                required: true,
                                validator: async (_, value: string) => {
                                    if (value == null || value == '') {
                                        return;
                                    }
                                    const exists = await dictItemApi.checkItemCodeExists(dictItemQuery['dictCode'] as string, value);
                                    if (exists) {
                                        return Promise.reject(new Error(t('DictItem.codeExistsErrorMessage')));
                                    }
                                }
                            }
                        ]}
                    >
                        <Input
                            suffix={<Tooltip title={t('DictItem.codeTips')}>
                                <IconFont type={'i-tips-hint'}/>
                            </Tooltip>}
                            disabled={updateId != null}
                            placeholder={t('DictItem.codePlaceholder')}
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
                            showCount
                            maxLength={500}/>
                    </Form.Item>
                </Form>
            </Modal>
        </>
    )
}