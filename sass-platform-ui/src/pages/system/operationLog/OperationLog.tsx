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
import {Button, DatePicker, Descriptions, List, message, Select, TableColumnsType, Tag} from "antd";
import React, {useCallback, useEffect, useRef, useState} from "react";
import {Modal, PageList} from "@/components";
import useRouteSearchParams from "@/hooks/useRouteSearchParams.tsx";
import {TableRefType} from '@/components/List/table/interface';
import {CheckCircleOutlined, CloseCircleOutlined, CopyOutlined, EyeOutlined} from '@ant-design/icons';
import {operationLogApi} from '@/apis/operationLog';
import {useDictItem} from "@/hooks/useDictItem.tsx";
import dayjs from "dayjs";


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
            render: (_, record: OperationLogModel) => {
                return findDictItemName('OPERATION_LOG_TYPE', record.operationType);
            }
        },
        {
            title: t('OperationLog.operationStatus'),
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
            title: t('OperationLog.riskType'),
            dataIndex: 'riskType',
            align: "center",
            showSorterTooltip: false,
            render: (riskType: string) => {
                switch (riskType) {
                    case 'LOW_LEVEL':
                        return <Tag color="warning">
                            {t('OperationLog.lowLevel')}
                        </Tag>
                    case 'MIDDLE_LEVEL':
                        return <Tag color="warning">
                            {t('OperationLog.middleLevel')}
                        </Tag>
                    case 'HIGH_LEVEL':
                        return <Tag color="error">
                            {t('OperationLog.highLevel')}
                        </Tag>

                }
            }
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
        {
            title: t('Common.action'),
            dataIndex: 'action',
            align: "center",
            render: (_, record: OperationLogModel) => {

                return <Button icon={<EyeOutlined/>} type={'link'}
                               onClick={async () => {
                                   setOperationLog(await operationLogApi.operationLogInfo(record.id!))
                                   setModalOpen(true)
                               }}
                >{t('OperationLog.detail')}</Button>
            }
        }
    ];

    const tableRef = useRef<TableRefType<OperationLogModel>>(null);
    const {querySearchParams, updateSearchParams} = useRouteSearchParams();
    const [pageQuery, setPageQuery] = useState<Record<string, string>>({...querySearchParams()});
    const [moduleNameList, setModuleNameList] = useState<string[]>([]);
    const {findDictItemName, findDictItems} = useDictItem(["OPERATION_LOG_TYPE"]);
    const [isModalOpen, setModalOpen] = useState<boolean>(false);
    const [operationLog, setOperationLog] = useState<OperationLogModel>({});
    const {RangePicker} = DatePicker;


    const initSearchSelect = useCallback(async () => {
        setModuleNameList(await operationLogApi.getModuleList());
    }, [])

    useEffect(() => {
        initSearchSelect().then();
    }, []);

    /**
     * 复制
     * @param value 值
     */
    const handleCopy = async (value: string) => {
        await navigator.clipboard.writeText(value);
        await message.success(t('Common.copySuccess'));
    };

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
                        <>
                            <span>{t('OperationLog.moduleName')}</span>
                            <Select
                                allowClear
                                defaultValue={pageQuery.moduleName}
                                key={'moduleName'}
                                placeholder={t('OperationLog.moduleNamePlaceholder')}
                                onChange={(value) => {
                                    setPageQuery({...pageQuery, moduleName: value})
                                }}
                                options={moduleNameList.map(value => {
                                    return {
                                        value: value,
                                        label: value
                                    }
                                })}
                            />
                        </>,
                        <>
                            <span>{t('OperationLog.operationType')}</span>
                            <Select
                                allowClear
                                defaultValue={pageQuery.operationType}
                                key={'operationType'}
                                placeholder={t('OperationLog.operationTypePlaceholder')}
                                onChange={(value) => {
                                    setPageQuery({...pageQuery, operationType: value})
                                }}
                                options={findDictItems('OPERATION_LOG_TYPE').map(itemValue => {
                                    return {
                                        value: itemValue.itemCode,
                                        label: itemValue.itemName
                                    }
                                })}
                            />
                        </>,
                        <>
                            <span>{t('OperationLog.operationStatus')}</span>
                            <Select
                                allowClear
                                defaultValue={pageQuery.isSuccess}
                                key={'isSuccess'}
                                placeholder={t('OperationLog.operationStatusPlaceholder')}
                                onChange={(value) => {
                                    setPageQuery({...pageQuery, isSuccess: value})
                                }}
                                options={[
                                    {value: 'true', label: t('Common.success')},
                                    {value: 'false', label: t('Common.failed')},
                                ]}
                            />
                        </>,
                        <>
                            <span>{t('OperationLog.operationTime')}</span>
                            <RangePicker
                                allowClear
                                allowEmpty
                                defaultValue={[
                                    pageQuery['startTime'] ? dayjs(pageQuery['startTime']) : null,
                                    pageQuery['endTime'] ? dayjs(pageQuery['endTime']) : null
                                ]}
                                onCalendarChange={(_, search, __) => {
                                    setPageQuery({...pageQuery, startTime: search[0], endTime: search[1]})
                                }}/>
                        </>
                    ],
                    onSearchClick: () => updateSearchParams(pageQuery)
                }}
            />

            <Modal
                title={t('OperationLog.info')}
                open={isModalOpen}
                closable
                onCancel={() => setModalOpen(false)}
                destroyOnClose
                width={'80%'}
                footer={[]}
            >
                <Descriptions column={2} size={'small'} bordered>
                    <Descriptions.Item
                        label={t('OperationLog.systemName')}>{operationLog.systemName}</Descriptions.Item>
                    <Descriptions.Item
                        label={t('OperationLog.moduleName')}>{operationLog.moduleName}</Descriptions.Item>

                    <Descriptions.Item
                        label={t('OperationLog.requestMethod')}>{operationLog.requestMethod}</Descriptions.Item>
                    <Descriptions.Item
                        label={t('OperationLog.requestUri')}>{operationLog.requestUri}</Descriptions.Item>

                    <Descriptions.Item label={t('OperationLog.requestIp')}>{operationLog.requestIp}</Descriptions.Item>
                    <Descriptions.Item
                        label={t('OperationLog.requestLocation')}>{operationLog.requestLocation}</Descriptions.Item>

                    <Descriptions.Item
                        span={2}
                        label={t('OperationLog.requestParam')}
                    >
                        <List className={'request-params-list'}>
                            {operationLog.requestParam && Object.entries(JSON.parse(operationLog.requestParam as string)).map(([key, value], index) => (
                                <List.Item key={index}>
                                    <div className={'log-item-value'}
                                    >
                                        <List.Item.Meta
                                            title={key}
                                            description={value ? value as string : ''}
                                        />
                                        <Button
                                            icon={<CopyOutlined/>}
                                            size="small"
                                            style={{marginLeft: 10}}
                                            onClick={() => handleCopy(value as string)}
                                            title={t('Common.copy')}
                                        />
                                    </div>

                                </List.Item>
                            ))}
                        </List>

                    </Descriptions.Item>
                    <Descriptions.Item
                        span={2}
                        styles={{
                            content: {
                                maxWidth: '25rem'
                            }
                        }}
                        label={t('OperationLog.responseData')}>
                        {operationLog.responseData && <div className={'log-item-value'}
                        >
                            {operationLog.responseData}
                            <Button
                                icon={<CopyOutlined/>}
                                size="small"
                                style={{marginLeft: 10}}
                                onClick={() => handleCopy(operationLog.responseData as string)}
                                title={t('Common.copy')}
                            />
                        </div>}
                    </Descriptions.Item>

                    <Descriptions.Item
                        span={2}
                        styles={{
                            content: {
                                maxWidth: '25rem'
                            }
                        }}
                        label={t('OperationLog.errorMessage')}>
                        {operationLog.errorMessage}
                    </Descriptions.Item>

                    <Descriptions.Item
                        label={t('OperationLog.operationUser')}>{operationLog.operationUser}</Descriptions.Item>
                    <Descriptions.Item
                        label={t('OperationLog.operationTime')}>{operationLog.operationTime}</Descriptions.Item>
                </Descriptions>
            </Modal>

        </>
    )
}