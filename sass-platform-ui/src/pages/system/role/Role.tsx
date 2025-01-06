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
import './index.scss'
import {
    Button,
    Form,
    Input,
    InputNumber,
    message,
    Popconfirm,
    Radio,
    Select,
    TableColumnsType,
    Tag,
    Tooltip
} from "antd";
import {Role as RoleModel} from "@/model/role";
import {roleApi} from "@/apis/role";
import {PageQuery, PageResult} from "@/model/pageQuery";
import type {TableRowSelection} from "antd/es/table/interface";
import {FormTree, IconFont, Modal, PageList, PermissionButton} from "@/components";
import {AddButton, DeleteButton, EditButton} from "@components/Button/commonButton";
import {useTranslation} from "react-i18next";
import {permissionApi} from "@/apis/permission";
import {Menu} from "@/model/menu";
import {useButton} from "@/hooks/useButton.tsx";
import {RolePermissionConstant} from "@/constants/permissionConstant.tsx";

export const Role: React.FC = () => {
    const {t} = useTranslation();
    const buttonPermissions = useButton(RolePermissionConstant.List);
    const initForm: RoleModel = {
        displayOrder: 1,
        isEnabled: true,
        dataScope: 'ALL',
    }
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
            sorter: true,
            defaultSortOrder: "descend",
            showSorterTooltip: false
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
                return (
                    <PermissionButton permissionStr={RolePermissionConstant.EDIT} buttonPermissions={buttonPermissions}>
                        <EditButton onClick={() => openModal(record.id)}/>
                    </PermissionButton>
                )
            }
        }
    ];

    const [updateId, setUpdateId] = useState<string | undefined>();
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
    const [isModalButtonLoading, setIsModalButtonLoading] = useState<boolean>(false);
    const [form] = Form.useForm();
    const [roleQuery, setRoleQuery] = useState<{ [key: string]: unknown }>({});
    const [pageResult, setPageResult] = useState<PageResult<RoleModel>>();
    const [permissionIds, setPermissionIds] = useState<React.Key[]>([]);
    const [treeSelectData, setTreeSelectData] = useState<Menu[]>([]);
    const [formInitValues, setFormInitValues] = useState<RoleModel>(initForm);
    /**
     * 打开模态组
     * @param roleId 角色id
     */
    const openModal = async (roleId?: string) => {
        setUpdateId(roleId);
        const treeData = await permissionApi.getPermissionTreeSelect();
        setTreeSelectData(treeData);
        if (roleId) {
            const roleInfo: RoleModel = await roleApi.getInfoByIdApi(roleId);
            setFormInitValues(roleInfo);
            setPermissionIds(roleInfo.permissionIds as Key[]);
        } else {
            setFormInitValues(initForm);
            setPermissionIds([]);
        }
        setIsModalOpen(true);
    }

    /**
     * 处理角色表单
     */
    const handlerForm = async () => {
        await form.validateFields();
        const role: RoleModel = form.getFieldsValue();
        role.permissionIds = permissionIds;
        setIsModalButtonLoading(true);
        try {
            await (updateId ? roleApi.editInfoApi(updateId, role) : roleApi.saveInfoApi(role));
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
                            <PermissionButton permissionStr={RolePermissionConstant.ADD}
                                              buttonPermissions={buttonPermissions}>
                                <AddButton onClick={() => openModal()}/>
                            </PermissionButton>
                            <PermissionButton permissionStr={RolePermissionConstant.DELETE}
                                              buttonPermissions={buttonPermissions}>
                                <Popconfirm
                                    title={t('Button.delete')}
                                    description={t('Button.deleteConfirm')}
                                    okText={t('Common.yes')}
                                    cancelText={t('Common.no')}
                                    onConfirm={async () => {
                                        await roleApi.deleteInfoApi(rowKeys as string[]);
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
                        <><label htmlFor="roleCode">{t('Role.code')}</label>
                            <Input
                                allowClear={true}
                                placeholder={t('Role.codePlaceholder')}
                                id={'roleCode'}
                                onChange={(e) => {
                                    setRoleQuery({roleCode: e.target.value})
                                }}/>
                        </>,
                        <>
                            <span>{t('Common.status')}</span>
                            <Select
                                allowClear={true}
                                key={'isEnabled'}
                                placeholder={t('Common.statusPlaceholder')}
                                onChange={(value) => roleQuery['isEnabled'] = value}
                                options={[
                                    {value: true, label: <span>{t('Common.enabled')}</span>},
                                    {value: false, label: <span>{t('Common.disabled')}</span>}
                                ]}
                            />
                        </>
                    ],
                    onSearchClick: () => setPageQuery({...pageQuery, ...roleQuery})
                }}
            />

            <Modal
                destroyOnClose={true}
                title={updateId ? t('Role.edit') : t('Role.add')}
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
                <Form<RoleModel>
                    name="basic"
                    form={form}
                    labelCol={{span: 7}}
                    wrapperCol={{span: 16}}
                    clearOnDestroy={true}
                    autoComplete="off"
                    initialValues={{...formInitValues}}
                >
                    <Form.Item
                        label={t('Role.name')}
                        name="roleName"
                        validateTrigger="onBlur"
                        key="roleName"
                        colon={false}
                        required={true}
                        hasFeedback
                        validateFirst={true}
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
                        validateFirst={true}
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
                                    if (updateId != null || value == null || value == '') {
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
                        label={t('Role.dataScope')}
                        name="dataScope"
                        key="dataScope"
                        colon={false}
                        required={true}
                    >
                        <Select
                            key={'dataScope'}
                            placeholder={t('Role.dataScopePlaceholder')}
                            options={[
                                {value: 'ALL', label: <span>全部</span>},
                                {value: 'DEPT', label: <span>当前部门</span>},
                                {value: 'DEPT_AND_SUB', label: <span>部门及下辖</span>},
                                {value: 'ONLY_USER', label: <span>仅本人</span>},
                            ]}
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
                        label={t('Role.permissionIds')}
                        key="permissionIds"
                        name="permissionIds"
                        colon={false}
                        required={true}
                        valuePropName={'checked'}
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
                            onSelectedAll={(ids: string[]) => {
                                console.log(ids);
                                setPermissionIds(ids);
                            }}
                        />
                    </Form.Item>


                </Form>
            </Modal>
        </>
    )
}
