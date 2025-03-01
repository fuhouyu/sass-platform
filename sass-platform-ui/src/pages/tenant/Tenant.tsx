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


import React, {useState} from "react";
import {Input, Popconfirm, TableColumnsType, Tag} from "antd";
import {TenantInfo} from "@/model/tenant";
import {PageList, PermissionButton} from "@/components";
import './index.scss'
import {tenantApi} from "@/apis/tenant";
import {AddButton, DeleteButton, EditButton} from "@components/Button/commonButton";
import type {TableRowSelection} from "antd/es/table/interface";
import {Userinfo} from "@/model/user";
import {useTranslation} from "react-i18next";
import {useButton} from "@/hooks/useButton.tsx";
import {TenantPermissionConstant} from "@/constants/permissionConstant.tsx";
import {CheckCircleOutlined} from "@ant-design/icons";
import {useDictItem} from "@/hooks/useDictItem.tsx";
import {useNavigate} from "react-router-dom";
import useRouteSearchParams from "@/hooks/useRouteSearchParams.tsx";
import {usePageList} from "@/hooks/usePageList.tsx";

/**
 * 租户组件
 * @constructor
 */
export const Tenant: React.FC = () => {
    const buttonPermissions = useButton(TenantPermissionConstant.List);
    const {t} = useTranslation();
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
            render: (_, record: TenantInfo) => {
                return findDictItemName('TENANT_TYPE', record.tenantType);
            }
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
                isEnabled ?
                    <Tag icon={<CheckCircleOutlined/>} color="success">
                        {t('Common.enabled')}
                    </Tag>
                    :
                    <Tag icon={<CheckCircleOutlined/>} color="error">
                        {t('Common.disabled')}
                    </Tag>
            )
        },
        {
            title: t('Common.updateAt'),
            dataIndex: 'updateAt',
            align: "center",
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
            render: (_, record: TenantInfo) => {
                return (<PermissionButton buttonPermissions={buttonPermissions}
                                          permissionStr={TenantPermissionConstant.EDIT}>
                    <EditButton onClick={() => navigate(`/tenant-form/${record.id}`)}/>
                </PermissionButton>)
            }
        }
    ];

    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const {querySearchParams, updateSearchParams} = useRouteSearchParams();

    const [tenantQuery, setTenantQuery] = useState<Record<string, string>>({...querySearchParams()});
    const {findDictItemName} = useDictItem(["TENANT_TYPE"]);
    const navigate = useNavigate();
    const {refreshPageList} = usePageList<TenantInfo>(tenantApi.pageInfoListApi);

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
                pageApi: tenantApi.pageInfoListApi,
                rowSelection: rowSelection,
                tableComponents: [
                    <>
                        <PermissionButton buttonPermissions={buttonPermissions}
                                          permissionStr={TenantPermissionConstant.ADD}>
                            <AddButton onClick={() => navigate('/tenant-form')}/>
                        </PermissionButton>
                        <PermissionButton buttonPermissions={buttonPermissions}
                                          permissionStr={TenantPermissionConstant.DELETE}>
                            <Popconfirm
                                title={t('Button.delete')}
                                description={t('Button.deleteConfirm')}
                                okText={t('Common.yes')}
                                cancelText={t('Common.no')}
                                onConfirm={async () => {
                                    await tenantApi.deleteInfoApi(rowKeys as string[]);
                                    await refreshPageList();
                                }}
                            >
                                <DeleteButton disabled={rowKeys === undefined || rowKeys.length === 0}/>
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
    </>)
}
