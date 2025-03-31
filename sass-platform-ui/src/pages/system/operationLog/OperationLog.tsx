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

import './index.scss'
import {useTranslation} from "react-i18next";
import {OperationLog as OperationLogModel} from "@/model/operationLog";
import {Input, TableColumnsType, Tag} from "antd";
import React, {useRef, useState} from "react";
import {PageList} from "@/components";
import useRouteSearchParams from "@/hooks/useRouteSearchParams.tsx";
import {TableRefType} from '@/components/List/table/interface';
import {CheckCircleOutlined, CloseCircleOutlined} from '@ant-design/icons';
import {operationLogApi} from '@/apis/operationLog';

/**
 * 操作日志
 * @constructor 构造函数
 */
export const OperationLog = () => {

    const {t} = useTranslation();

    const columns: TableColumnsType = [
        {
            title: t('OperationLog.moduleName'),
            dataIndex: 'moduleName',
            align: "center"
        },
        {
            title: t('OperationLog.operationType'),
            dataIndex: 'operationType',
            align: "center",
        },
        {
            title: t('OperationLog.isSuccess'),
            dataIndex: 'isSuccess',
            align: 'center',
            render: (isSuccess: boolean) => (
                isSuccess ?
                    <Tag icon={<CheckCircleOutlined/>} color="success">
                        {t('Common.success')}
                    </Tag>
                    :
                    <Tag icon={<CloseCircleOutlined/>} color="error">
                        {t('Common.failed')}
                    </Tag>
            )
        },
        {
            title: t('OperationLog.requestIp'),
            dataIndex: 'requestIp',
            align: "center",
            showSorterTooltip: false
        },
        {
            title: t('OperationLog.requestLocation'),
            dataIndex: 'requestLocation',
            align: "center",
            showSorterTooltip: false
        },
        {
            title: t('OperationLog.operationUser'),
            dataIndex: 'operationUser',
            align: "center",
            showSorterTooltip: false
        },
        {
            title: t('OperationLog.operationTime'),
            dataIndex: 'operationTime',
            align: "center",
        },
    ];

    const tableRef = useRef<TableRefType<OperationLogModel>>(null);
    const {querySearchParams, updateSearchParams} = useRouteSearchParams();
    const [pageQuery, setPageQuery] = useState<Record<string, string>>({...querySearchParams()});


    return (
        <>
            <PageList
                tableProps={{
                    tableRef: tableRef,
                    tableName: t('OperationLog.list'),
                    columns: columns,
                    pageApi: operationLogApi.pageApi,
                }}
                headerSearchProps={{
                    components: [
                        <><label htmlFor="moduleName">{t('OperationLog.moduleName')}</label>
                            <Input
                                allowClear
                                defaultValue={pageQuery.moduleName}
                                placeholder={t('OperationLog.moduleNamePlaceholder')}
                                id={'moduleName'}
                                onChange={(e) => {
                                    setPageQuery({moduleName: e.target.value})
                                }}/>
                        </>,
                        // <>
                        //     <span>{t('Common.status')}</span>
                        //     <Select
                        //         allowClear
                        //         defaultValue={pageQuery.isEnabled}
                        //         key={'isEnabled'}
                        //         placeholder={t('Common.statusPlaceholder')}
                        //         onChange={(value) => pageQuery['isEnabled'] = value}
                        //         options={[
                        //             {value: 'true', label: <span>{t('Common.enabled')}</span>},
                        //             {value: 'false', label: <span>{t('Common.disabled')}</span>}
                        //         ]}
                        //     />
                        // </>
                    ],
                    onSearchClick: () => updateSearchParams(pageQuery)
                }}
            />
        </>
    )
}