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
import {useTranslation} from "react-i18next";
import {Position as PositionModel} from "@/model/position";
import {Button, Form, Input, message, Popconfirm, TableColumnsType, Tooltip} from "antd";
import {AddButton, DeleteButton, EditButton} from "@components/Button/commonButton";
import React, {useEffect, useState} from "react";
import {PageQuery, PageResult} from "@/model/pageQuery";
import type {TableRowSelection} from "antd/es/table/interface";
import {IconFont, Modal, PageList, PermissionButton} from "@/components";
import TextArea from "antd/es/input/TextArea";
import {positionApi} from "@/apis/position";
import {useParams} from "react-router-dom";
import {useButton} from '@/hooks/useButton';
import {PositionPermissionConstant} from "@/constants/permissionConstant.tsx";
import {useAppSelector} from "@/store";

/**
 * 岗位
 * @constructor 构造函数
 */
export const Position = () => {

    const {t} = useTranslation();
    const buttonPermissions = useButton(PositionPermissionConstant.List);
    const columns: TableColumnsType = [
        {
            title: t('Position.name'),
            dataIndex: 'positionName',
            showSorterTooltip: {target: 'full-header'},
            align: "center",
        },
        {
            title: t('Position.code'),
            dataIndex: 'positionCode',
            defaultSortOrder: 'descend',
            align: "center",
        },

        {
            title: t('Common.updateAt'),
            dataIndex: 'updateAt',
            sorter: true,
            defaultSortOrder: "descend",
            align: "center",
            showSorterTooltip: false
        },
        {
            title: t('Common.updateBy'),
            dataIndex: 'updateBy',
            align: "center",
        },
        {
            title: t('Common.action'),
            dataIndex: 'action',
            align: "center",
            render: (_, record: PositionModel) => {
                return <PermissionButton permissionStr={PositionPermissionConstant.EDIT}
                                         buttonPermissions={buttonPermissions}>
                    <EditButton onClick={() => openModal(record.id)}/>
                </PermissionButton>

            }
        }
    ];

    const [updateId, setUpdateId] = useState<string | undefined>();
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const [form] = Form.useForm();
    const [pageResult, setPageResult] = useState<PageResult<PositionModel>>();
    const params = useParams();
    const [positionQuery, setPositionQuery] = useState<{ [key: string]: unknown }>({});
    const initForm: PositionModel = {
        ...positionQuery,
        ...params
    }
    const [formInitValues, setFormInitValues] = useState<PositionModel>(initForm);
    const language = useAppSelector(state => state.locale.language);

    /**
     * 打开模态组
     * @param positionId 角色id
     */
    const openModal = async (positionId?: string) => {
        setUpdateId(positionId);
        if (positionId) {
            const positionInfo: PositionModel = await positionApi.getInfoByIdApi(positionId);
            setFormInitValues(positionInfo);
        } else {
            setFormInitValues(initForm);
        }
        setIsModalOpen(true);
    }

    /**
     * 处理角色表单
     */
    const handlerForm = async () => {
        await form.validateFields();
        const position: PositionModel = form.getFieldsValue();
        setIsModalButtonLoading(true);
        try {
            await (updateId ? positionApi.editInfoApi(updateId, position) : positionApi.saveInfoApi(position));
            message.success(t('Common.success')).then()
            await pageRequest();
            setIsModalOpen(false);
        } finally {
            setIsModalButtonLoading(false)
        }
    }

    /**
     * 分页查询
     */
    const [pageQuery, setPageQuery] = useState<PageQuery>({
        pageNum: 1,
        pageSize: 10,
        ...params
    });


    useEffect(() => {
        //  分页查询
        positionApi.pageInfoListApi({...pageQuery})
            .then((res: PageResult<PositionModel>) => {
                setPageResult({...res});
            });
    }, [pageQuery]);

    /**
     * 分页查询请求
     */
    const pageRequest = async () => {
        const res = await positionApi.pageInfoListApi(pageQuery);
        setPageResult({...res});
    }

    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<PositionModel> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
    };

    return (
        <>
            <PageList
                tableProps={{
                    tableName: t('Position.list'),
                    columns: columns,
                    pageData: pageResult,
                    setPageQuery: setPageQuery,
                    rowSelection: rowSelection,
                    components: [
                        <>
                            <PermissionButton permissionStr={PositionPermissionConstant.ADD}
                                              buttonPermissions={buttonPermissions}>
                                <AddButton onClick={() => openModal()}/>
                            </PermissionButton>

                            <PermissionButton permissionStr={PositionPermissionConstant.DELETE}
                                              buttonPermissions={buttonPermissions}>
                                <Popconfirm
                                    title={t('Button.delete')}
                                    description={t('Button.deleteConfirm')}
                                    okText={t('Common.yes')}
                                    cancelText={t('Common.no')}
                                    onConfirm={async () => {
                                        positionApi.deleteInfoApi(rowKeys as string[]).then();
                                        await pageRequest()
                                    }}
                                >
                                    <DeleteButton/>
                                </Popconfirm>
                            </PermissionButton>

                        </>
                    ]
                }}
                headerSearchProps={{
                    components: [
                        <><label htmlFor="positionName">{t('Position.name')}</label>
                            <Input
                                allowClear={true}
                                placeholder={t('Position.namePlaceholder')}
                                id={'positionName'}
                                onChange={(e) => {
                                    setPositionQuery({positionName: e.target.value})
                                }}/>
                        </>,
                        <><label htmlFor="positionCode">{t('Position.code')}</label>
                            <Input
                                allowClear={true}
                                placeholder={t('Position.codePlaceholder')}
                                id={'positionCode'}
                                onChange={(e) => {
                                    setPositionQuery({positionCode: e.target.value})
                                }}/>
                        </>
                    ],
                    onSearchClick: () => setPageQuery({...pageQuery, ...positionQuery})
                }}
            />

            <Modal
                centered
                destroyOnClose={true}
                title={updateId ? t('Position.edit') : t('Position.add')}
                className="ant-modal-header"
                open={isModalOpen}
                onCancel={() => setIsModalOpen(false)}
                footer={[
                    <Button key='onOk' type="primary" loading={isModalButtonLoading}
                            onClick={handlerForm}>{t('Button.submit')}</Button>,
                    <Button key='onCancel' onClick={() => setIsModalOpen(false)}>{t('Button.cancel')}</Button>
                ]}
                closeIcon={<IconFont type="i-Close" style={{
                    fontSize: '24px',
                }}/>}
            >
                <Form<PositionModel>
                    name="modal-form"
                    form={form}
                    labelCol={{span: language === 'zh' ? 4 : 6}}
                    clearOnDestroy={true}
                    autoComplete="off"
                    initialValues={{...formInitValues}}
                >

                    <Form.Item
                        label={t('Position.name')}
                        name="positionName"
                        validateTrigger="onBlur"
                        key="positionName"
                        colon={false}
                        required={true}
                        hasFeedback
                        validateFirst={true}
                        rules={[{
                            required: true,
                            type: "string",
                            message: t('Position.namePlaceholder'),
                            max: 50,
                        }
                        ]}
                    >
                        <Input placeholder={t('Position.namePlaceholder')} maxLength={50}/>
                    </Form.Item>

                    <Form.Item
                        label={t('Position.code')}
                        name="positionCode"
                        validateTrigger="onBlur"
                        key="positionCode"
                        colon={false}
                        required={true}
                        validateFirst={true}
                        hasFeedback
                        rules={updateId ? [] : [{
                            required: true,
                            type: "string",
                            message: t('Position.codePlaceholder'),
                            max: 50,
                        },
                            {
                                required: true,
                                validator: async (_, value: string) => {
                                    if (value == null || value == '') {
                                        return;
                                    }
                                    const exists = await positionApi.checkPositionCode(value);
                                    if (exists) {
                                        return Promise.reject(new Error(t('Position.codeExistsErrorMessage')));
                                    }
                                }
                            }
                        ]}
                    >
                        <Input
                            suffix={<Tooltip title={t('Position.codeTips')}>
                                <IconFont type={'i-tips-hint'}/>
                            </Tooltip>}
                            disabled={updateId != null}
                            placeholder={t('Position.codePlaceholder')}
                            maxLength={50}/>
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