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
import {Space, TableColumnsType} from "antd";

import {PageList} from "@/components";
import {SearchInput} from "@components/List/pageParams";
import {getTenantConfigListApi, removerTenantConfigApi} from "@/apis/tenantConfig";
import {TenantConfigModel} from "@/model/tenant";

export const TenantConfig: React.FC = () => {

    const columns: TableColumnsType = [
        {
            title: '配置名称',
            dataIndex: 'name',
            showSorterTooltip: {target: 'full-header'},
        },
        {
            title: '是否启用',
            dataIndex: 'isEnabled',
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
            render: (_, record: TenantConfigModel) => {
                console.log(record);
                return (<>
                    <Space size="middle" style={{whiteSpace: 'nowrap'}}>
                        <a onClick={() => {
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
                    name: '配置名称',
                    key: 'keyword',
                    comment: SearchInput,
                    placeholder: '配置名称',
                }
            ]}
            listName='租户配置'
            columns={columns}
            pageRequestApi={getTenantConfigListApi}
            addCallback={() => {
                // openModal(undefined, true)
            }}
            deleteCallback={(ids: string[]) => removerTenantConfigApi(ids)}
        />
    </>)
}