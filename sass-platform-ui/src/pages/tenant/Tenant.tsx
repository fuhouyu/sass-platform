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


import React, {useRef, useState} from "react";
import {Button, Drawer, Input, message, Popconfirm, Space, Switch, TableColumnsType} from "antd";
import {TenantInfo} from "@/model/tenant";
import {PageList, PermissionButton} from "@/components";
import './index.scss'
import {tenantApi} from "@/apis/tenant";
import {AddButton, DeleteButton, EditButton} from "@components/Button/commonButton";
import type {TableRowSelection} from "antd/es/table/interface";
import {useTranslation} from "react-i18next";
import {useButton} from "@/hooks/useButton.tsx";
import {TenantPermissionConstant} from "@/constants/permissionConstant.tsx";
import {useDictItem} from "@/hooks/useDictItem.tsx";
import useRouteSearchParams from "@/hooks/useRouteSearchParams.tsx";
import {TableRefType} from "@components/List/table/interface.tsx";
import TenantForm from "./components/form";
import {permissionApi} from "@/apis/permission.tsx";
import {Menu} from "@/model/menu.tsx";
import {ReloadOutlined} from "@ant-design/icons";
import {useUserStore} from "@/store";

/**
 * 租户组件
 * @constructor
 */
export const Tenant: React.FC = () => {
    const buttonPermissions = useButton(TenantPermissionConstant.List);
    const {t} = useTranslation();
    const tableRef = useRef<TableRefType<TenantInfo>>(null);
    const [updateId, setUpdatedId] = useState<string | undefined>();
    const {tenant} = useUserStore(state => state);
    const columns: TableColumnsType = [
        {
            title: t('Tenant.code'),
            dataIndex: 'tenantCode',
            showSorterTooltip: {target: 'full-header'},
            align: 'center',
        },
        {
            title: t('Tenant.name'),
            dataIndex: 'tenantName',
            defaultSortOrder: 'descend',
            align: 'center',
        },
        {
            title: t('Tenant.type'),
            dataIndex: 'tenantType',
            align: 'center',
            render: (_, record: TenantInfo) => {
                return findDictItemName('TENANT_TYPE', record.tenantType);
            }
        },
        {
            title: t('Tenant.contactPerson'),
            align: 'center',
            dataIndex: 'contactPerson',
        },
        {
            title: t('Tenant.contactInfo'),
            dataIndex: 'contactInfo',
            align: 'center',
        },
        {
            title: t('Common.status'),
            dataIndex: 'isEnabled',
            align: 'center',
            render: (_, record: TenantInfo) => (
                <Switch
                    disabled={tenant?.id === record.id}
                    defaultChecked={record.isEnabled} onChange={async (checked) => {
                    await tenantApi.status(record.id!, checked);
                    await tableRef?.current?.refreshPageList();
                }}/>
            )
        },
        {
            title: t('Tenant.expiration'),
            dataIndex: 'expiration',
            align: 'center',
            render: (_, record: TenantInfo) => {
                {
                    if (record.startDate && record.endDate) {
                        return record.startDate + "-" + record.endDate;
                    }
                    return t('Tenant.permanent');
                }
            }
        },
        {
            title: t('Common.updatedAt'),
            dataIndex: 'updatedAt',
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
            width: 240,
            fixed: 'right',
            render: (_, record: TenantInfo) => {
                const flag = record.id === tenant?.id
                return (
                    <>
                        <PermissionButton buttonPermissions={buttonPermissions}
                                          permissionStr={TenantPermissionConstant.EDIT}>
                            <Space>
                                <Popconfirm
                                    title={t('Tenant.resetPassword')}
                                    description={t('Tenant.resetPasswordConfirm')}
                                    okText={t('Common.yes')}
                                    cancelText={t('Common.no')}
                                    onConfirm={async () => {
                                        await tenantApi.resetPassword(record.id!);
                                        message.success('密码重置成功');
                                    }}
                                >
                                    <Button
                                        disabled={flag}
                                        icon={<ReloadOutlined/>} color="pink"
                                            variant={'outlined'}>{t('Tenant.resetPassword')}</Button>
                                </Popconfirm>
                                <EditButton
                                    disabled={flag}
                                    onClick={() => openDrawer(record.id)}/>
                            </Space>
                        </PermissionButton>

                    </>

                )
            }
        }
    ];

    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const {querySearchParams, updateSearchParams} = useRouteSearchParams();

    const [tenantQuery, setTenantQuery] = useState<Record<string, string>>({...querySearchParams()});
    const {findDictItemName} = useDictItem(["TENANT_TYPE"]);
    const [openTenantDrawer, setOpenTenantDrawer] = useState<boolean>(false);
    const [permissionTree, setPermissionTree] = useState<Menu[] | undefined>(undefined);


    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<TenantInfo> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
        getCheckboxProps: (record: TenantInfo) => ({
            disabled: tenant?.id === record.id
        }),
    };


    /**
     * 打开租户表单抽屉
     */
    const openDrawer = async (tenantId?: string | undefined) => {
        setUpdatedId(tenantId)
        setOpenTenantDrawer(true);
        setPermissionTree(await permissionApi.getPermissionTreeSelect());
    }

    /**
     * 关闭租户表单抽屉
     */
    const closeDrawer = async () => {
        setOpenTenantDrawer(false);
        await tableRef.current?.refreshPageList();
    }


    return (<>
        <PageList
            tableProps={{
                tableRef: tableRef,
                tableName: t('Tenant.list'),
                columns: columns,
                pageApi: tenantApi.pageInfoListApi,
                rowSelection: rowSelection,
                tableComponents: [
                    <>
                        <PermissionButton buttonPermissions={buttonPermissions}
                                          permissionStr={TenantPermissionConstant.ADD}>
                            <AddButton onClick={() => openDrawer(undefined)}/>
                        </PermissionButton>
                        <PermissionButton
                            buttonPermissions={buttonPermissions}
                                          permissionStr={TenantPermissionConstant.DELETE}>
                            <Popconfirm
                                title={t('Button.delete')}
                                description={t('Button.deleteConfirm')}
                                okText={t('Common.yes')}
                                cancelText={t('Common.no')}
                                onConfirm={async () => {
                                    await tenantApi.deleteInfoApi(rowKeys as string[]);
                                    await tableRef?.current?.refreshPageList();
                                }}
                            >
                                <DeleteButton
                                    disabled={rowKeys === undefined || rowKeys.length === 0}
                                />
                            </Popconfirm>
                        </PermissionButton>
                    </>
                ]
            }}
            headerSearchProps={{
                components: [
                    <><label htmlFor="tenantName">{t('Tenant.name')}</label>
                        <Input defaultValue={tenantQuery.tenantName}
                               allowClear
                               placeholder={t('Tenant.namePlaceholder')}
                               id={'tenantName'} onChange={(e) => {
                            setTenantQuery({tenantName: e.target.value})
                        }}/>
                    </>
                ],
                onSearchClick: () => updateSearchParams({...tenantQuery}),
            }}
        />

        <Drawer title={updateId === undefined ? t('Tenant.add') : t('Tenant.edit')}
                width={'50%'}
                destroyOnClose
                closable
                maskClosable
                onClose={closeDrawer}
                open={openTenantDrawer}>
            <TenantForm
                tenantId={updateId}
                callback={closeDrawer}
                permissionTreeData={permissionTree}/>
        </Drawer>
    </>)
}
