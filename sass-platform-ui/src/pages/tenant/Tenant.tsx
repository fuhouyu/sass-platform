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


import React, {Key, useEffect, useState} from "react";
import {Button, Form, Input, message, Popconfirm, Radio, TableColumnsType, Tag} from "antd";
import {TenantInfo} from "@/model/tenant";
import {FormTree, IconFont, Modal, PageList, PermissionButton} from "@/components";
import {Menu} from "@/model/menu";
import TextArea from "antd/es/input/TextArea";
import './index.scss'
import {tenantApi} from "@/apis/tenant";
import {PageQuery, PageResult} from "@/model/pageQuery";
import {AddButton, DeleteButton, EditButton} from "@components/Button/commonButton";
import type {TableRowSelection} from "antd/es/table/interface";
import {Userinfo} from "@/model/user";
import {useTranslation} from "react-i18next";
import {Role as RoleModel} from "@/model/role";
import {permissionApi} from "@/apis/permission";
import {useButton} from "@/hooks/useButton.tsx";
import {TenantPermissionConstant} from "@/constants/permissionConstant.tsx";
import {useDictItem} from "@/hooks/useDictItem.tsx";

/**
 * 租户组件
 * @constructor
 */
export const Tenant: React.FC = () => {
    const buttonPermissions = useButton(TenantPermissionConstant.List);
    const {t} = useTranslation();
    const {getDictItemByDictCode} = useDictItem('TENANT_TYPE');
    const columns: TableColumnsType = [
        {
            title: t('Tenant.code'),
            dataIndex: 'tenantCode',
            showSorterTooltip: {target: 'full-header'},
        },
        {
            title: t('Tenant.name'),
            dataIndex: 'tenantName',
            defaultSortOrder: 'descend',
        },
        {
            title: t('Tenant.type'),
            dataIndex: 'tenantType',
        },
        {
            title: t('Tenant.contactPerson'),
            dataIndex: 'contactPerson',
        },
        {
            title: t('Tenant.contactInfo'),
            dataIndex: 'contactInfo',
        },
        {
            title: t('Common.status'),
            dataIndex: 'isEnabled',
            align: 'center',
            render: (isEnabled: boolean) => (
                isEnabled ? <Tag color={"#E8F4FF"} style={{border: "1px solid blue"}}>
                        <span style={{color: '#2090FF'}}>{t('Common.enabled')}</span>
                    </Tag> :
                    <Tag color={"#FFEDED"} style={{border: "1px solid #FFB6B6"}}>
                        <span style={{color: '#FF9696'}}>{t('Common.disabled')}</span>
                    </Tag>
            )
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
            render: (_, record: RoleModel) => {
                return (<>
                    <PermissionButton buttonPermissions={buttonPermissions}
                                      permissionStr={TenantPermissionConstant.EDIT}>
                        <EditButton onClick={() => openModal(record.id)}/>
                    </PermissionButton>

                </>)
            }
        }
    ];
    const initForm: TenantInfo = {
        isEnabled: true,
    }

    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const [updateId, setUpdateId] = useState<string>();
    const [form] = Form.useForm();
    const [tenantQuery, setTenantQuery] = useState<{ [key: string]: unknown }>({});
    const [pageResult, setPageResult] = useState<PageResult<TenantInfo>>();
    const [formInitValues, setFormInitValues] = useState<TenantInfo>(initForm);
    const [permissionIds, setPermissionIds] = useState<React.Key[]>([]);
    const [treeSelectData, setTreeSelectData] = useState<Menu[]>([]);


    /**
     * 分页查询
     */
    const [pageQuery, setPageQuery] = useState<PageQuery>({
        pageNum: 1,
        pageSize: 10,
    });

    /**
     * 分页查询结果
     */
    useEffect(() => {
        tenantApi.pageInfoListApi(pageQuery)
            .then((res: PageResult<TenantInfo>) => {
                setPageResult({...res});
            });
    }, [pageQuery])

    /**
     * 分页查询请求
     */
    const pageRequest = async () => {
        const res = await tenantApi.pageInfoListApi(pageQuery);
        setPageResult({...res});
    }


    /**
     * 打开模态组
     * @param tenantId 租户id
     */
    const openModal = async (tenantId?: string) => {
        setUpdateId(tenantId);
        const treeData = await permissionApi.getPermissionTreeSelect();
        setTreeSelectData(treeData);
        if (!tenantId) {
            setIsModalOpen(true);
            return
        }
        // 修改获取租户数据，先获取详情
        const res = await tenantApi.getInfoByIdApi(tenantId!);
        setFormInitValues(res);
        setPermissionIds(res.permissionIds ?? [])
        setIsModalOpen(true);
    }


    /**
     * 关闭模态组
     */
    const closeModal = () => {
        setIsModalOpen(false);
        setFormInitValues(initForm);
        setPermissionIds([]);
    }


    /**
     * 处理租户
     */
    const handleTenant = async () => {
        const tenantInfo: TenantInfo = form.getFieldsValue();
        tenantInfo.permissionIds = permissionIds;
        await form.validateFields();
        setIsModalButtonLoading(true);
        try {
            await (updateId ? tenantApi.editInfoApi(updateId, tenantInfo) : tenantApi.saveInfoApi(tenantInfo));
            message.success(t('Common.success')).then();
            await pageRequest();
            setIsModalOpen(false);
        } finally {
            setIsModalButtonLoading(false);
        }
    }

    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<Userinfo> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
    };

    return (<>
        <PageList
            tableProps={{
                tableName: t('Tenant.list'),
                columns: columns,
                pageData: pageResult,
                setPageQuery: setPageQuery,
                rowSelection: rowSelection,
                components: [
                    <>
                        <PermissionButton buttonPermissions={buttonPermissions}
                                          permissionStr={TenantPermissionConstant.ADD}>
                            <AddButton onClick={() => openModal()}/>
                        </PermissionButton>
                        <PermissionButton buttonPermissions={buttonPermissions}
                                          permissionStr={TenantPermissionConstant.DELETE}>
                            <Popconfirm
                                title={t('Button.delete')}
                                description={t('Button.deleteConfirm')}
                                okText={t('Common.yes')}
                                cancelText={t('Common.no')}
                                onConfirm={async () => {
                                    tenantApi.deleteInfoApi(rowKeys as string[]).then();
                                    await pageRequest();
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
                    <><label htmlFor="tenantName">{t('Tenant.name')}</label>
                        <Input placeholder={t('Tenant.namePlaceholder')} id={'tenantName'} onChange={(e) => {
                            setTenantQuery({tenantName: e.target.value})
                        }}/>
                    </>
                ],
                onSearchClick: () => setPageQuery({...pageQuery, ...tenantQuery})
            }}
        />

        <Modal
            title={updateId ? t('Tenant.edit') : t('Tenant.add')}
            open={isModalOpen}
            onCancel={() => closeModal()}
            footer={[
                <Button key='onOk' type="primary" loading={isModalButtonLoading}
                        onClick={handleTenant}>{t('Button.submit')}</Button>,
                <Button key='onCancel' onClick={() => closeModal()}>{t('Button.cancel')}</Button>
            ]}
            closeIcon={<IconFont type="i-Close" style={{
                fontSize: '1.5rem',
            }}/>}
            destroyOnClose={true}
        >
            <Form
                clearOnDestroy={true}
                name="basic"
                form={form}
                autoComplete="off"
                onFinish={handleTenant}
                labelCol={{span: 8}}
                wrapperCol={{span: 13}}
                initialValues={formInitValues}
            >
                <Form.Item
                    label={t('Tenant.name')}
                    name="tenantName"
                    validateTrigger="onBlur"
                    key="tenantName"
                    colon={false}
                    required={true}
                    hasFeedback
                    rules={[{required: true, message: t('Tenant.namePlaceholder')}]}
                >
                    <Input placeholder={t('Tenant.namePlaceholder')} maxLength={20}/>
                </Form.Item>
                <Form.Item
                    label={t('Tenant.code')}
                    name="tenantCode"
                    validateTrigger="onBlur"
                    key="tenantCode"
                    colon={false}
                    required={true}
                    hasFeedback={true}
                    validateFirst={true}
                    rules={updateId ? [] : [
                        {required: true, message: t('Tenant.codePlaceholder')},
                        {
                            required: true,
                            validator: async (_, value: string) => {
                                if (value == null || value == '') {
                                    return;
                                }
                                const exists = await tenantApi.checkTenantCodeExists(value);
                                if (exists) {
                                    return Promise.reject(new Error(t('Tenant.codeExistsErrorMessage')));
                                }
                            }
                        }
                    ]}
                >
                    <Input disabled={updateId != null} placeholder={t('Tenant.codePlaceholder')} maxLength={20}/>
                </Form.Item>
                <Form.Item
                    label={t('Tenant.permissions')}
                    key="permissionIds"
                    name='permissionIds'
                    colon={false}
                    required={true}
                    hasFeedback={true}
                >
                    <FormTree<Menu>
                        formTreeProps={{
                            fieldNames: {key: 'id'},
                            checkedKeys: permissionIds,
                            onCheck: (checked: {
                                checked: Key[];
                                halfChecked: Key[];
                            } | Key[]) => {
                                if (checked instanceof Array) {
                                    setPermissionIds(checked as React.Key[]);
                                    return
                                }
                                setPermissionIds(checked.checked);
                            },
                            titleRender: (menu: Menu) => t(`Menu.${menu.permissionName}`),
                            treeData: treeSelectData,
                        }}
                        onSelectedAll={(ids: string[]) => setPermissionIds(ids)}
                    />
                </Form.Item>
                <Form.Item
                    label={t('Tenant.contactPerson')}
                    name="contactPerson"
                    validateTrigger="onBlur"
                    key="contactPerson"
                    colon={false}
                    required={true}
                    hasFeedback
                    rules={[{required: true, message: t('Tenant.contactPersonPlaceholder')}]}
                >
                    <Input placeholder={t('Tenant.contactPersonPlaceholder')} maxLength={20}/>
                </Form.Item>
                <Form.Item
                    label={t('Tenant.contactInfo')}
                    name="contactInfo"
                    validateTrigger="onBlur"
                    key="contactInfo"
                    colon={false}
                    required={true}
                    hasFeedback
                    rules={[{required: true, message: t('Tenant.contactInfoPlaceholder')}]}
                >
                    <Input placeholder={t('Tenant.contactInfoPlaceholder')} maxLength={20}/>
                </Form.Item>
                <Form.Item
                    label={t('Common.status')}
                    name='isEnabled'
                    key="isEnabled"
                    colon={false}
                    hasFeedback
                >
                    <Radio.Group>
                        <Radio value={true}>{t('Common.enabled')}</Radio>
                        <Radio value={false}>{t('Common.enabled')}</Radio>
                    </Radio.Group>
                </Form.Item>
                <Form.Item
                    label={t('Common.remark')}
                    name="remark"
                    key="remark"
                    colon={false}
                >
                    <TextArea className="remark" placeholder={t('Common.remark')} showCount maxLength={500}/>
                </Form.Item>
            </Form>
        </Modal>
    </>)
}
