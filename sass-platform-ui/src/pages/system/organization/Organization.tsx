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
 * See the License for the specific language governing organizations and
 * limitations under the License.
 */

import React, {Key, useEffect, useState} from "react";
import {Organization as OrganizationModal} from "@/model/organization.tsx";
import {PageQuery, PageResult} from "@/model/pageQuery.tsx";
import {useTranslation} from "react-i18next";
import {useButton} from "@/hooks/useButton.tsx";
import {
    Button,
    Form,
    Input,
    InputNumber,
    message,
    Popconfirm,
    Radio,
    Splitter,
    TableColumnsType,
    Tree,
    TreeSelect
} from "antd";
import {AnyObject} from "antd/es/_util/type";
import {IconFont, Modal, PermissionButton, SearchHeader, Table} from "@/components";
import {DeleteButton, EditButton} from "@components/Button/commonButton.tsx";
import type {TableRowSelection} from "antd/es/table/interface";
import {DownOutlined} from "@ant-design/icons";
import {OrganizationPermissionConstant} from "@/constants/permissionConstant.tsx";
import {organizationApi} from "@/apis/organization.tsx";
import './index.scss'
import TextArea from "antd/es/input/TextArea";
import {useOrganizationLazyData} from "@/hooks/useOrganizationLazyData.tsx";
import {useLocaleStore} from "@/store";
import {CommonConstant} from "@/constants/commonConstant.tsx";


export const Organization = () => {
    const [treeSelectData, setTreeSelectData] = useState<OrganizationModal[]>([]);
    const [pageQuery, setPageQuery] = useState<PageQuery>({
        pageNum: 1,
        pageSize: 10,
        parentId: '-1',
        sortColumn: 'display_order',
        isAsc: true,
    });

    const {t} = useTranslation();
    const buttonPermissions = useButton(OrganizationPermissionConstant.List);
    const [search, setSearch] = useState<{ [key: string]: unknown; }>({});
    const [pageData, setPageData] = useState<PageResult<OrganizationModal>>({} as PageResult<OrganizationModal>);
    const [rowKeys, setRowKeys] = useState<React.Key[]>([]);
    const [updateId, setUpdateId] = useState<string | undefined>();
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [form] = Form.useForm();
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const [formParentOrganization, setFormParentOrganization] = useState<OrganizationModal>({} as OrganizationModal);
    const {onLoadData, organizationLazyData} = useOrganizationLazyData();
    const language = useLocaleStore((state) => state.language);


    const columns: TableColumnsType<OrganizationModal> = [
        {
            title: t('Organization.name'),
            dataIndex: 'organizationName',
            align: 'center',
        },
        {
            title: t('Organization.code'),
            dataIndex: 'organizationCode',
            align: 'center',
        },
        {
            title: t('Common.displayOrder'),
            dataIndex: 'displayOrder',
            align: 'center',
            sorter: true,
            defaultSortOrder: 'descend',
        },
        {
            title: t('Common.updateAt'),
            dataIndex: 'updateAt',
            align: 'center',
            sorter: true,
            showSorterTooltip: false
        },
        {
            title: t('Common.updateBy'),
            align: 'center',
            dataIndex: 'updateBy',
        },
        {
            title: t('Common.action'),
            align: 'center',
            fixed: 'right',
            width: 120,
            dataIndex: 'action',
            render: (_: AnyObject, record: OrganizationModal) => {
                return (

                    <PermissionButton buttonPermissions={buttonPermissions}
                                      permissionStr={OrganizationPermissionConstant.EDIT}>
                        <EditButton onClick={() => openModal(record.id)}/>
                    </PermissionButton>
                )
            }
        }
    ];

    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<OrganizationModal> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
    };


    /**
     * 表单搜索
     * @param tableSearch 表单搜索
     */
    const tableSearch = (tableSearch: { [key: string]: unknown }) => {
        setPageQuery({
            ...pageQuery,
            ...search,
            ...tableSearch
        });
    }

    /**
     * 右侧列表
     */
    useEffect(() => {
        const pageOrganization = async () => {
            const organizationPageResult = await organizationApi.pageInfoListApi(pageQuery);
            setPageData(organizationPageResult);
        }
        pageOrganization().then();
    }, [pageQuery]);


    /**
     * 树被点击时的事件
     * @param selectedKeys 当前选中的key
     * @param node 选中的树节点
     */
    const onSelectTree = async (selectedKeys: Key[], {node}: { node: OrganizationModal }) => {
        if (!selectedKeys || selectedKeys.length === 0) {
            return
        }
        setFormParentOrganization(node);
        // 这里只会有一条
        setPageQuery({...pageQuery, parentId: selectedKeys[0].toLocaleString()})
    }


    /**
     * 权限树
     */
    const organizationTreeSelect = async () => {
        const res = await organizationApi.getOrganizationTreeSelect()
        setTreeSelectData(res)
    }

    /**
     * 打开模态框
     * @param updateId 修改的id
     */
    const openModal = async (updateId?: string | undefined) => {
        setUpdateId(updateId);
        await organizationTreeSelect();
        if (updateId) {
            const organizationDetails = await organizationApi.getInfoByIdApi(updateId);
            form.setFieldsValue({...organizationDetails})
        }
        setIsModalOpen(true);
    }

    /**
     * 关闭模态组
     */
    const closeModal = () => {
        setIsModalOpen(false);
        setUpdateId(undefined);
        setFormParentOrganization({} as OrganizationModal)
    }


    /**
     * 处理表单
     */
    const handleForm = async () => {
        let values: OrganizationModal;
        try {
            values = await form.validateFields();
        } catch {
            return
        }
        try {
            setIsModalButtonLoading(true);
            await (updateId ? organizationApi.editInfoApi(updateId, values) : organizationApi.saveInfoApi(values));
            const res = await organizationApi.pageInfoListApi(pageQuery);
            const parentId = values.parentId;
            await onLoadData({key: parentId});
            setPageData(res);
            setIsModalOpen(false);
            message.success(t('Common.success')).then()
        } finally {
            setIsModalButtonLoading(false);
        }
    }


    return (
        <>
            <Splitter>
                <Splitter.Panel className={'tree-container'} defaultSize="15%" min="10%" max="70%">
                    <div className='tree-info'>
                        <Tree.DirectoryTree
                            showIcon={false}
                            defaultExpandParent={true}
                            blockNode
                            motion={false}
                            fieldNames={{key: 'id', title: 'organizationName'}}
                            switcherIcon={<DownOutlined/>}
                            loadData={onLoadData}
                            treeData={organizationLazyData}
                            onSelect={onSelectTree}
                        />
                    </div>
                </Splitter.Panel>
                <Splitter.Panel>
                        <SearchHeader
                            components={[
                                <><label htmlFor="organizationName">{t('Organization.name')}</label>
                                    <Input placeholder={t('Organization.namePlaceholder')} id={'organizationName'}
                                           onChange={(e) => setSearch({organizationName: e.target.value})}/>
                                </>,
                            ]}
                            onSearchClick={() => {
                                setPageQuery({...pageQuery, ...search})
                            }}
                        />
                        <Table<OrganizationModal>
                            tableName={t('Organization.list')}
                            columns={columns}
                            rowSelection={rowSelection}
                            setPageQuery={tableSearch}
                            pageData={pageData}
                            scroll={{x: 1500}}
                            tableComponents={[
                                <>
                                    <PermissionButton buttonPermissions={buttonPermissions}
                                                      permissionStr={OrganizationPermissionConstant.ADD}>
                                        <Button className="add-button"
                                                onClick={() => openModal()}
                                                icon={<IconFont type="i-add"/>}
                                        >
                                            {t('Organization.add')}
                                        </Button>
                                    </PermissionButton>
                                    <PermissionButton buttonPermissions={buttonPermissions}
                                                      permissionStr={OrganizationPermissionConstant.DELETE}>
                                        <Popconfirm
                                            title={t('Button.delete')}
                                            description={t('Button.deleteConfirm')}
                                            okText={t('Common.yes')}
                                            cancelText={t('Common.no')}
                                            onConfirm={async () => {
                                                await organizationApi.deleteInfoApi(rowKeys as string[]).then();
                                                setPageQuery({...pageQuery});
                                                await onLoadData({key: formParentOrganization.id});
                                            }}
                                        >
                                            <DeleteButton disabled={rowKeys === undefined || rowKeys.length === 0}/>
                                        </Popconfirm>
                                    </PermissionButton>
                                </>
                            ]}
                        />
                </Splitter.Panel>
            </Splitter>
            <Modal
                destroyOnClose={true}
                title={updateId ? t('Organization.edit') : t('Organization.add')}
                open={isModalOpen}
                onCancel={() => closeModal()}
                footer={[
                    <Button key='onOk' type="primary"
                            loading={isModalButtonLoading}
                            onClick={handleForm}
                    >{t('Button.confirm')}</Button>,
                    <Button key='onCancel' onClick={() => closeModal()}>{t('Button.cancel')}</Button>
                ]}
            >
                <Form
                    clearOnDestroy={true}
                    name="modal-form"
                    form={form}
                    labelCol={{span: language == CommonConstant.ZH_CN_LANGUAGE ? 4 : 8}}
                    autoComplete="off"
                    initialValues={{
                        parentId: formParentOrganization.id,
                        isVisible: true,
                        isEnabled: true,
                    }}
                >
                    <Form.Item
                        label={t('Organization.parentOrganization')}
                        name="parentId"
                        validateTrigger="onBlur"
                        colon={false}
                        required={true}
                    >
                        <TreeSelect
                            title={'parentOrganizationName'}
                            fieldNames={{
                                label: 'organizationName',
                                value: 'id',
                            }}
                            onSelect={(_: string, node: OrganizationModal) => setFormParentOrganization(node)}
                            allowClear
                            dropdownStyle={{maxHeight: 400, overflow: 'auto'}}
                            treeData={treeSelectData}
                            treeDefaultExpandAll
                        />
                    </Form.Item>

                    <Form.Item
                        label={t('Organization.name')}
                        name="organizationName"
                        key="organizationName"
                        colon={false}
                        required={true}
                        rules={[
                            {
                                required: true,
                                type: "string",
                                message: t('Organization.nameCheckMessage')
                            }
                        ]}
                    >
                        <Input placeholder={t('Organization.namePlaceholder')} maxLength={20}/>
                    </Form.Item>
                    <Form.Item
                        label={t('Common.displayOrder')}
                        name="displayOrder"
                        key="displayOrder"
                        colon={false}
                        required={true}
                        validateTrigger="onBlur"
                        rules={[
                            {
                                required: true,
                                type: "number",
                                message: t('Common.displayOrderPlaceholder')
                            }
                        ]}
                    >
                        <InputNumber placeholder={t('Common.displayOrderPlaceholder')} style={{width: '100%'}}
                                     min={1}/>
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
                        <TextArea className="remark"
                                  placeholder={t('Common.remark')}
                                  showCount maxLength={500}/>
                    </Form.Item>
                </Form>

            </Modal>
        </>
    )
}