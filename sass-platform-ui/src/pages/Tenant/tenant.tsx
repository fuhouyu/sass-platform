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


import React from "react";
import {PageList} from "@/components";
import {removeUserApi} from "@/apis/user";
import {Space, TableColumnsType} from "antd";
import {TenantDetail} from "@/model/tenant";
import {getTenantListApi} from "@/apis/tenant";
import {SearchInput} from "@components/List/pageParams";

export const Tenant: React.FC = () => {

    const columns: TableColumnsType = [
        {
            title: '租户编码',
            dataIndex: 'tenantCode',
            showSorterTooltip: {target: 'full-header'},
        },
        {
            title: '租户名称',
            dataIndex: 'tenantName',
            defaultSortOrder: 'descend',
        },
        {
            title: '租户类型',
            dataIndex: 'tenantType',
        },
        {
            title: '联系人',
            dataIndex: 'contactPerson',
        },
        {
            title: '联系方式',
            dataIndex: 'contactNumber',
        },
        {
            title: '创建时间',
            dataIndex: 'createAt',
            sorter: true,
            showSorterTooltip: false
        },
        {
            title: '创建人',
            dataIndex: 'createBy'
        },
        {
            title: '更新时间',
            dataIndex: 'updateAt',
            sorter: true,
            defaultSortOrder: "descend",
            showSorterTooltip: false
        },
        {
            title: '操作人',
            dataIndex: 'updateBy',
        },
        {
            title: '操作',
            dataIndex: 'action',
            render: (_, record: TenantDetail) => {
                return (<>
                    <Space size="middle" style={{whiteSpace: 'nowrap'}}>
                        <a onClick={() => {
                            console.log(record)
                        }}>修改</a>
                    </Space>
                </>)
            }
        }
    ];

    return (<>
        <PageList
            // ref={pageListRef}
            searchComments={[
                {
                    name: '租户名称',
                    key: 'tenantName',
                    comment: SearchInput,
                    placeholder: '租户名称',
                }
            ]}
            listName='租户'
            columns={columns}
            pageRequestApi={getTenantListApi}
            addCallback={() => {
                // openModal(undefined, true)
            }}
            deleteCallback={(ids: string[]) => removeUserApi(ids)}
        />
    </>)
}