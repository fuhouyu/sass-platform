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

import React, {FC, useRef, useState} from "react";
import {usePageTitle} from "@/hooks/usePageTitle.tsx";
import {useTranslation} from "react-i18next";
import {Button, DatePicker, Input, Popconfirm, TableColumnsType, Tooltip} from "antd";
import './index.scss'
import {TableRefType} from "@components/List/table/interface.tsx";
import {OperationLog as OperationLogModel} from "@/model/operationLog.tsx";
import useRouteSearchParams from "@/hooks/useRouteSearchParams.tsx";
import {PageList, PermissionButton} from "@/components";
import {onlineUserApi} from "@/apis/onlineUserApi.tsx";
import dayjs from "dayjs";
import {OnlineUser as OnlineUserModel} from "@/model/onlineUser.tsx";
import type {TableRowSelection} from "antd/es/table/interface";
import {getAccessToken} from "@/utils";
import {useButton} from "@/hooks/useButton.tsx";
import {OnlineUserPermissionConstant} from "@/constants/permissionConstant.tsx";

const {RangePicker} = DatePicker;

export const OnlineUser: FC = () => {

    usePageTitle('Menu.onlineUser');
    const {t} = useTranslation();

    const columns: TableColumnsType = [
        {
            title: t('OnlineUser.sessionId'),
            ellipsis: {
                showTitle: false,
            },
            dataIndex: 'accessToken',
            align: "center",
            render: (accessToken) => (
                <Tooltip placement="topLeft" title={accessToken}>
                    {accessToken}
                </Tooltip>
            ),
        },
        {
            title: t('OnlineUser.loginAccount'),
            dataIndex: 'loginAccount',
            align: "center"
        },
        {
            title: t('OnlineUser.loginType'),
            dataIndex: 'loginType',
            align: "center",
        },
        {
            title: t('OnlineUser.loginTime'),
            dataIndex: 'loginTime',
            width: 200,
            align: "center",
        },
        {
            title: t('OnlineUser.loginIp'),
            dataIndex: 'loginIp',
            align: "center",
        },
        {
            title: t('OnlineUser.loginLocation'),
            dataIndex: 'loginLocation',
            align: "center"
        },
        {
            title: t('OnlineUser.os'),
            dataIndex: 'os',
            align: "center"
        },
        {
            title: t('OnlineUser.browser'),
            dataIndex: 'browser',
            align: "center"
        },
        {
            title: t('OnlineUser.browserVersion'),
            dataIndex: 'browserVersion',
            align: "center",
        },
        {
            title: t('OnlineUser.engine'),
            dataIndex: 'engine',
            align: "center"
        },

        {
            title: t('OnlineUser.platform'),
            dataIndex: 'platform',
            align: "center",
        },
        {
            title: t('Common.action'),
            dataIndex: 'action',
            align: "center",
            render: (_, record: OnlineUserModel) => {
                return <Button
                    disabled={record.accessToken === getAccessToken()}
                    type="primary"
                    danger
                    size={'small'}
                    onClick={async () => {
                        onlineUserApi.logout([record.accessToken!])
                            .then(() => {
                                tableRef.current?.refreshPageList();
                            })
                    }}
                >
                    {t('OnlineUser.forceLogout')}
                </Button>
            }
        }
    ];

    const buttonPermissions = useButton(OnlineUserPermissionConstant.List);
    const tableRef = useRef<TableRefType<OperationLogModel>>(null);
    const {querySearchParams, updateSearchParams} = useRouteSearchParams();
    const [pageQuery, setPageQuery] = useState<Record<string, string>>({...querySearchParams()});
    const [rowKeys, setRowKeys] = useState<React.Key[]>([])


    /**
     * table列选择
     */
    const rowSelection: TableRowSelection<OnlineUserModel> = {
        onChange: (selectedRowKeys: React.Key[]) => setRowKeys(selectedRowKeys),
        getCheckboxProps: (record: OnlineUserModel) => ({
            disabled: record.accessToken === getAccessToken()
        }),
    };

    return (
        <PageList
            tableProps={{
                rowKey: 'accessToken',
                tableRef: tableRef,
                tableName: t('OnlineUser.list'),
                columns: columns,
                pageApi: onlineUserApi.onlineUserList,
                rowSelection: rowSelection,
                tableComponents: [
                    <PermissionButton
                        key={'logout'}
                        buttonPermissions={buttonPermissions}
                        permissionStr={OnlineUserPermissionConstant.ForceLogout}>

                        <Popconfirm
                            title={t('OnlineUser.forceLogout')}
                            description={t('OnlineUser.forceLogoutTips')}
                            okText={t('Common.yes')}
                            cancelText={t('Common.no')}
                            onConfirm={async () => {
                                onlineUserApi.logout(rowKeys)
                                    .then(() => {
                                        tableRef.current?.refreshPageList();
                                    })
                            }}
                        >
                            <Button
                                key="logout"
                                disabled={rowKeys === undefined || rowKeys.length === 0}
                                type="primary"
                                danger
                            >
                                {t('OnlineUser.forceLogout')}
                            </Button>
                        </Popconfirm>
                    </PermissionButton>
                ]
            }}
            headerSearchProps={{
                components: [
                    <>
                        <span>{t('OnlineUser.loginAccount')}</span>
                        <Input
                            allowClear
                            defaultValue={pageQuery['loginAccount']}
                            placeholder={t('OnlineUser.loginAccountPlaceholder')}
                            onChange={(e) => {
                                setPageQuery({...pageQuery, loginAccount: e.target.value});
                            }}/>
                    </>
                    ,
                    <>
                        <span>{t('OnlineUser.loginTime')}</span>
                        <RangePicker
                            showTime
                            allowClear
                            allowEmpty
                            disabledDate={(currentDate) => {
                                return currentDate.isAfter(new Date());
                            }}
                            defaultValue={[
                                pageQuery['startTime'] ? dayjs(decodeURIComponent(pageQuery['startTime'])) : null,
                                pageQuery['endTime'] ? dayjs(decodeURIComponent(pageQuery['endTime'])) : null,
                            ]}
                            onChange={(_, dateStrings) => {
                                setPageQuery({...pageQuery, startTime: dateStrings[0], endTime: dateStrings[1]});
                            }}/>
                    </>
                ],
                onSearchClick: () => updateSearchParams(pageQuery)
            }}

        />)
}