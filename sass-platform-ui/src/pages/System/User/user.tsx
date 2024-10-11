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
import {Input, Table, TableColumnsType, TableProps} from "antd";
import "./index.scss"

interface DataType {
    key: React.Key;
    name: string;
    age: number;
    address: string;
}

const columns: TableColumnsType<DataType> = [
    {
        title: '用户名',
        dataIndex: 'username',
        showSorterTooltip: {target: 'full-header'},
    },
    {
        title: '真实姓名',
        dataIndex: 'realName',
        defaultSortOrder: 'descend',
    },
    {
        title: '昵称',
        dataIndex: 'nickname',
    },
    {
        title: '性别',
        dataIndex: 'gender',
    },
    {
        title: '登录时间',
        dataIndex: 'loginDate',
    },
    {
        title: '登录ip',
        dataIndex: 'loginIp',
    },
];


const onChange: TableProps<DataType>['onChange'] = (pagination, filters, sorter, extra) => {
    console.log('params', pagination, filters, sorter, extra);
};


const User: React.FC = () => {


    return (
        <>
            <div className="user-container">
                <div className="search-header">
                    <span>关键字查询</span>
                    <Input placeholder="请输入用户关键字"/>
                </div>
                <div className="divide"/>
                <div className="user-list">
                    <Table<DataType>
                        columns={columns}
                        dataSource={data}
                        onChange={onChange}
                        showSorterTooltip={{target: 'sorter-icon'}}
                    />
                </div>
            </div>
        </>
    )
}

export default User;