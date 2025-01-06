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
    Col,
    Form,
    Input,
    InputNumber,
    message,
    Popconfirm,
    Radio,
    Row,
    TableColumnsType,
    Tree,
    TreeSelect
} from "antd";
import {AnyObject} from "antd/es/_util/type";
import {IconFont, Modal, PermissionButton, SearchHeader, Table} from "@/components";
import {AddButton, DeleteButton, EditButton} from "@components/Button/commonButton.tsx";
import type {TableRowSelection} from "antd/es/table/interface";
import {DownOutlined} from "@ant-design/icons";
import {OrganizationPermissionConstant} from "@/constants/permissionConstant.tsx";
import {organizationApi} from "@/apis/organization.tsx";
import './index.scss'
import TextArea from "antd/es/input/TextArea";

/**
 * 设置树数据
 * @param list 菜单集合
 * @param key key
 * @param children 子集
 */
const updateTreeData = (list: OrganizationModal[], key: React.Key, children: OrganizationModal[]): OrganizationModal[] => {
    return list.map((node: OrganizationModal) => {
        if (node.id === key) {
            return {
                ...node,
                children,
            };
        }
        if (node.children) {
            return {
                ...node,
                children: updateTreeData(node.children, key, children),
            };
        }
        return node;
    });

}

export const Organization = () => {
    const [treeSelectData, setTreeSelectData] = useState<OrganizationModal[]>([]);
    const [pageQuery, setPageQuery] = useState<PageQuery>({
        pageNum: 1,
        pageSize: 10,
        parentId: '-1',
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
    const [lazyTreeData, setLazyTreeData] = useState<OrganizationModal[]>([]);

    const columns: TableColumnsType<OrganizationModal> = [
        {
            title: t('Organization.name'),
            dataIndex: 'organizationName',
        },
        {
            title: t('Organization.code'),
            dataIndex: 'organizationCode',

        },
        {
            title: t('Common.displayOrder'),
            dataIndex: 'displayOrder',
            sorter: true,
            defaultSortOrder: 'descend',
        },
        {
            title: t('Common.updateAt'),
            dataIndex: 'updateAt',
            sorter: true,
            showSorterTooltip: false
        },
        {
            title: t('Common.updateBy'),
            dataIndex: 'updateBy',
        },
        {
            title: t('Common.action'),
            dataIndex: 'action',
            render: (_: AnyObject, record: OrganizationModal) => {
                return (<>
                    <PermissionButton buttonPermissions={buttonPermissions}
                                      permissionStr={OrganizationPermissionConstant.EDIT}>
                        <EditButton onClick={() => openModal(record.id)}/>
                    </PermissionButton>
                </>)
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
     * 左侧菜单树
     */
    useEffect(() => {
        // 先查询出一级菜单
        const initOrganization = async () => {
            const organizations = await organizationApi.getOrganizationListApi();
            console.log(organizations);
            setLazyTreeData(organizations);
        }
        initOrganization().then();
    }, []);

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
     * 懒加载菜单
     * @param key key，这里是主键id
     * @param children 子菜单
     */
    const onLoadData = async ({key, children}: { key: React.Key, children?: OrganizationModal[] | undefined }) => {
        if (children) {
            return new Promise<void>((resolve) => {
                resolve()
            })
        }
        const res = await organizationApi.getOrganizationListApi(key.toString());
        setLazyTreeData((origin) => updateTreeData(origin, key, res));
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
    const handlerForm = async () => {
        let values: OrganizationModal;
        try {
            values = await form.validateFields();
        } catch {
            return
        }
        try {
            setIsModalButtonLoading(true);
            await (updateId ? organizationApi.editInfoApi(updateId, values) : organizationApi.saveInfoApi(values));
            message.success(t('Common.success')).then()
            const res = await organizationApi.pageInfoListApi(pageQuery);
            setPageData(res);
            setIsModalOpen(false);
        } finally {
            setIsModalButtonLoading(false);
        }
    }


    return (
        <>
            <Row gutter={24} className={'main-container'}>
                <Col span={3} className={'tree-container'}>
                    {/*<Input*/}
                    {/*    className='search-input'*/}
                    {/*    placeholder={t('Organization.namePlaceholder')} allowClear/>*/}
                    <div className='tree-info'>
                        <Tree
                            defaultExpandParent={true}
                            showLine
                            fieldNames={{key: 'id', title: 'organizationName'}}
                            switcherIcon={<DownOutlined/>}
                            loadData={onLoadData}
                            treeData={lazyTreeData}
                            onSelect={onSelectTree}
                        />
                    </div>
                </Col>
                <Col span={21}>
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
                        components={[
                            <>
                                <PermissionButton buttonPermissions={buttonPermissions}
                                                  permissionStr={OrganizationPermissionConstant.ADD}>
                                    <AddButton onClick={() => openModal()}/>
                                </PermissionButton>
                                <PermissionButton buttonPermissions={buttonPermissions}
                                                  permissionStr={OrganizationPermissionConstant.DELETE}>
                                    <Popconfirm
                                        title={t('Button.delete')}
                                        description={t('Button.deleteConfirm')}
                                        okText={t('Common.yes')}
                                        cancelText={t('Common.no')}
                                        onConfirm={async () => {
                                            organizationApi.deleteInfoApi(rowKeys as string[]).then();
                                            setPageQuery({...pageQuery});
                                        }}
                                    >
                                        <DeleteButton/>
                                    </Popconfirm>
                                </PermissionButton>
                            </>
                        ]}
                    />
                </Col>
            </Row>

            <Modal
                destroyOnClose={true}
                title={updateId ? t('Organization.edit') : t('Organization.add')}
                className="ant-modal-header"
                open={isModalOpen}
                onCancel={() => closeModal()}
                width={400}
                footer={[
                    <Button key='onOk' type="primary"
                            loading={isModalButtonLoading}
                            onClick={handlerForm}
                    >{t('Button.confirm')}</Button>,
                    <Button key='onCancel' onClick={() => closeModal()}>{t('Button.cancel')}</Button>
                ]}
                closeIcon={<IconFont type="i-Close" style={{
                    fontSize: '24px',
                }}/>}
            >
                <Form
                    clearOnDestroy={true}
                    name="modal-form"
                    form={form}
                    autoComplete="off"
                    labelCol={{span: 10}}
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
                                  style={{height: 100}}
                                  showCount maxLength={500}/>
                    </Form.Item>
                </Form>

            </Modal>
        </>
    )
}