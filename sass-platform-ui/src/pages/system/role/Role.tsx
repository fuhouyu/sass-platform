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

import React, {useEffect, useState} from "react";
import './index.scss'
import {Button, Form, Input, InputNumber, message, Modal, Radio, TableColumnsType, Tag, Tooltip} from "antd";
import {Role as RoleModel} from "@/model/role";
import {roleApi} from "@/apis/role";
import {PageQuery, PageResult} from "@/model/pageQuery";
import type {TableRowSelection} from "antd/es/table/interface";
import {FormTree, IconFont, PageList} from "@/components";
import {AddButton, DeleteButton, EditButton} from "@components/Button/commonButton";
import {useTranslation} from "react-i18next";
import {permissionApi} from "@/apis/permission";
import {Menu} from "@/model/menu";

export const Role: React.FC = () => {
    const {t} = useTranslation();

    const columns: TableColumnsType = [
        {
            title: t('Role.name'),
            dataIndex: 'roleName',
            showSorterTooltip: {target: 'full-header'},
            align: "center",
        },
        {
            title: t('Role.code'),
            dataIndex: 'roleCode',
            defaultSortOrder: 'descend',
            align: "center",
        },
        {
            title: t('Common.displayOrder'),
            dataIndex: 'displayOrder',
            align: "center",
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
                    <EditButton onClick={() => openModal(record.id)}/>
                </>)
            }
        }
    ];

    const [updateId, setUpdateUserId] = useState<string | undefined>();
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const [form] = Form.useForm();
    const [roleQuery, setUserQuery] = useState<{ [key: string]: unknown }>({});
    const [pageResult, setPageResult] = useState<PageResult<RoleModel>>();
    const [treeSelectData, setTreeSelectData] = useState<Menu[]>([]);

    /**
     * 打开模态组
     * @param roleId 角色id
     */
    const openModal = async (roleId?: string) => {
        setUpdateUserId(roleId);
        const treeData = await permissionApi.getPermissionTreeSelect();
        setTreeSelectData(treeData);
        if (roleId) {
            const roleInfo: RoleModel = await roleApi.getInfoByIdApi(roleId);
            form.setFieldsValue({...roleInfo})
        }

        setIsModalOpen(true);
    }

    /**
     * 关闭模态组
     */
    const closeModal = () => {
        setIsModalOpen(false);
        form.resetFields();
    }

    /**
     * 处理角色表单
     */
    const handlerUserForm = async () => {
        await form.validateFields();
        setIsModalButtonLoading(true);
        try {
            await (updateId ? updateUserDetail() : saveUserDetail());
            message.success("操作成功").then()
            setIsModalOpen(false);
            form.resetFields();
        } catch (error: unknown) {
            message.error(error instanceof Error ? error.message : '未知错误').then()
        } finally {
            setIsModalButtonLoading(false);
        }
    }

    /**
     * 保存角色详情
     */
    const saveUserDetail: () => Promise<string> = () => {
        const roleDetail = form.getFieldsValue();
        return roleApi.saveInfoApi(roleDetail)
    }

    /**
     * 修改角色详情
     */
    const updateUserDetail: () => Promise<void> = () => {
        const roleInfo: RoleModel = form.getFieldsValue();
        return roleApi.editInfoApi(roleInfo.id!, roleInfo);
    }

    /**
     * 分页查询
     */
    const [pageQuery, setPageQuery] = useState<PageQuery>({
        pageNum: 1,
        pageSize: 10,
    });


    useEffect(() => {
        roleApi.pageInfoListApi(pageQuery)
            .then((res: PageResult<RoleModel>) => {
                setPageResult({...res});
            });
    }, [pageQuery])

    /**
     * 分页查询请求
     */
    const pageRequest = async () => {
        const res = await roleApi.pageInfoListApi(pageQuery);
        setPageResult({...res});
    }

    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<RoleModel> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
    };

    return (
        <>
            <PageList
                tableProps={{
                    tableName: t('Role.list'),
                    columns: columns,
                    pageData: pageResult,
                    setPageQuery: setPageQuery,
                    rowSelection: rowSelection,
                    components: [
                        <>
                            <AddButton onClick={() => openModal()}/>
                            <DeleteButton onClick={async () => {
                                roleApi.deleteInfoApi(rowKeys as string[]).then();
                                pageRequest()
                            }}/>
                        </>
                    ]
                }}
                headerSearchProps={{
                    components: [
                        <><label htmlFor="roleCode">{t('Role.code')}</label>
                            <Input placeholder={t('Role.codePlaceholder')} id={'roleCode'} onChange={(e) => {
                                setUserQuery({roleName: e.target.value})
                            }}/>
                        </>,
                    ],
                    onSearchClick: () => setPageQuery({...pageQuery, ...roleQuery})
                }}
            />

            <Modal
                title={updateId ? t('Role.add') : t('Role.edit')}
                className="ant-modal-header"
                open={isModalOpen}
                onCancel={() => closeModal()}
                width={450}
                footer={[
                    <Button key='onOk' type="primary" loading={isModalButtonLoading}
                            onClick={handlerUserForm}>{t('Button.submit')}</Button>,
                    <Button key='onCancel' onClick={() => closeModal()}>{t('Button.cancel')}</Button>
                ]}
                closeIcon={<IconFont type="i-Close" style={{
                    fontSize: '24px',
                }}/>}
            >
                <Form
                    name="basic"
                    form={form}
                    labelCol={{span: 5}}
                    autoComplete="off"
                    initialValues={{
                        displayOrder: 1,
                        isEnabled: true,
                    }}
                >
                    <Form.Item
                        label={t('Role.name')}
                        name="roleName"
                        validateTrigger="onBlur"
                        key="roleName"
                        colon={false}
                        required={true}
                        hasFeedback
                        rules={[{
                            required: true,
                            type: "string",
                            message: t('Role.namePlaceholder'),
                            max: 50,
                        }
                        ]}
                    >
                        <Input placeholder={t('Role.namePlaceholder')} maxLength={50}/>
                    </Form.Item>

                    <Form.Item
                        label={t('Role.code')}
                        name="roleCode"
                        validateTrigger="onBlur"
                        key="roleCode"
                        colon={false}
                        required={true}
                        hasFeedback
                        rules={[{
                            required: true,
                            type: "string",
                            message: t('Role.codePlaceholder'),
                            max: 50,
                        },
                            {
                                required: true,
                                validator: async (_, value: string) => {
                                    if (updateId != null) {
                                        return;
                                    }
                                    if (value == null || value == '') {
                                        return;
                                    }
                                    const exists = await roleApi.checkRoleCodeExists(value);
                                    if (exists) {
                                        return Promise.reject(new Error(t('Role.codeExistsErrorMessage')));
                                    }
                                }
                            }
                        ]}
                    >
                        <Input
                            suffix={<Tooltip title={t('Role.codeTips')}>
                                <IconFont type={'i-tips-hint'}/>
                            </Tooltip>}
                            placeholder={t('Role.namePlaceholder')}
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
                        rules={[{
                            required: true,
                            type: "number",
                            message: t('Common.displayOrderPlaceholder'),
                        }]}
                    >
                        <InputNumber placeholder={t('Common.displayOrderPlaceholder')} style={{width: '30%'}}
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
                        label={t('Role.permissionIds')}
                        key="permissionIds"
                        colon={false}
                        required={true}
                        valuePropName={'checkedKeys'}
                    >
                        <FormTree<Menu>
                            fieldNames={{
                                key: 'id'
                            }}
                            titleRender={(menu: Menu) => t(`Menu.${menu.permissionName}`)}
                            treeData={treeSelectData}

                        />
                    </Form.Item>

                </Form>
            </Modal>
        </>
    )
}
