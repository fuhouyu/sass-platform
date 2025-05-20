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

import {FC, useRef, useState} from "react";
import './index.scss';
import {Application as ApplicationModel} from "@/model/application";
import {useTranslation} from "react-i18next";
import {useButton} from "@/hooks/useButton.tsx";
import {ApplicationPermissionConstant} from "@/constants/permissionConstant.tsx";
import {Input, Popconfirm, Select, Switch, TableColumnsType} from "antd";
import {PageList, PermissionButton} from "@/components";
import {AddButton, DeleteButton, EditButton} from "@components/Button/commonButton.tsx";
import {TableRefType} from "@components/List/table/interface.tsx";
import useRouteSearchParams from "@/hooks/useRouteSearchParams.tsx";
import type {TableRowSelection} from "antd/es/table/interface";
import {applicationApi} from "@/apis/application.tsx";
import {
  ApplicationManageForm
} from "@/views/application/manage/components/ApplicationManageForm.tsx";

export const ApplicationManage: FC = () => {

    const {t} = useTranslation();
    const buttonPermissions = useButton(ApplicationPermissionConstant.List);
    const columns: TableColumnsType = [
        {
            title: t('Application.clientName'),
            dataIndex: 'clientName',
            showSorterTooltip: {target: 'full-header'},
            align: "center",
        },
        {
            title: t('Application.clientId'),
            dataIndex: 'clientId',
            align: "center",
        },
        {
            title: t('Common.status'),
            dataIndex: 'isEnabled',
            align: 'center',
            render: (_, record: ApplicationModel) => (
                <Switch defaultChecked={record.isEnabled} onChange={async (checked) => {
                    await applicationApi.status(record.clientId!, checked);
                    await tableRef?.current?.refreshPageList();
                }}/>
            )
        },

        {
            title: t('Common.updatedAt'),
            dataIndex: 'updatedAt',
            defaultSortOrder: 'descend',
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
            render: (_, record: ApplicationModel) => {
                return (
                    <PermissionButton permissionStr={ApplicationPermissionConstant.EDIT}
                                      buttonPermissions={buttonPermissions}>
                        <EditButton key={'edit'} onClick={() => {
                            setIsModalOpen(true);
                            setUpdateId(record.clientId);
                        }}/>
                    </PermissionButton>
                )
            }
        }
    ];

    const tableRef = useRef<TableRefType<ApplicationModel>>(null);
    const [updateId, setUpdateId] = useState<string | undefined>();
    const {querySearchParams, updateSearchParams} = useRouteSearchParams();
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])
    const [isModalOpen, setIsModalOpen] = useState<boolean>(false);

    const [pageQuery, setPageQuery] = useState<Record<string, string>>({...querySearchParams()});
    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<ApplicationModel> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
    };

    return (
        <>
            <PageList
                tableProps={{
                    tableRef: tableRef,
                    tableName: t('Application.list'),
                    columns: columns,
                    rowKey: 'clientId',
                    pageApi: applicationApi.pageInfoListApi,
                    rowSelection: rowSelection,
                    tableComponents: [
                        <>
                            <PermissionButton key={'add'} permissionStr={ApplicationPermissionConstant.ADD}
                                              buttonPermissions={buttonPermissions}>
                                <AddButton onClick={() => setIsModalOpen(true)}/>
                            </PermissionButton>
                            <PermissionButton key={'delete'} permissionStr={ApplicationPermissionConstant.DELETE}
                                              buttonPermissions={buttonPermissions}>
                                <Popconfirm
                                    title={t('Button.delete')}
                                    description={t('Button.deleteConfirm')}
                                    okText={t('Common.yes')}
                                    cancelText={t('Common.no')}
                                    onConfirm={async () => {
                                        await applicationApi.deleteInfoApi(rowKeys as string[]);
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
                        <><label htmlFor="clientName">{t('Application.clientName')}</label>
                            <Input
                                allowClear
                                defaultValue={pageQuery.clientName}
                                placeholder={t('Application.clientNamePlaceholder')}
                                id={'clientName'}
                                onChange={(e) => setPageQuery({clientName: e.target.value})}/>
                        </>,
                        <>
                            <span>{t('Common.status')}</span>
                            <Select
                                defaultValue={pageQuery.isEnabled}
                                allowClear
                                key={'isEnabled'}
                                placeholder={t('Common.statusPlaceholder')}
                                onChange={(value) => pageQuery['isEnabled'] = value}
                                options={[
                                    {value: 'true', label: <span>{t('Common.enabled')}</span>},
                                    {value: 'false', label: <span>{t('Common.disabled')}</span>}
                                ]}
                            />
                        </>
                    ],
                    onSearchClick: () => updateSearchParams(pageQuery),

                }}
            />

            <ApplicationManageForm
                isOpen={isModalOpen}
                updateId={updateId}
                onClose={async () => {
                    setIsModalOpen(false);
                    setUpdateId(undefined);
                    await tableRef?.current?.refreshPageList();
                }}
            />

        </>
    )
}
