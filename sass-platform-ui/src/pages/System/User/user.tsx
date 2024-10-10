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
import {Table, TableColumnsType, TableProps} from "antd";
import "./index.scss"

interface DataType {
    key: React.Key;
    name: string;
    age: number;
    address: string;
}

const columns: TableColumnsType<DataType> = [
    {
        title: 'Name',
        dataIndex: 'name',
        showSorterTooltip: {target: 'full-header'},
        onFilter: (value, record) => record.name.indexOf(value as string) === 0,
        sorter: (a, b) => a.name.length - b.name.length,
        sortDirections: ['descend'],
    },
    {
        title: 'Age',
        dataIndex: 'age',
        defaultSortOrder: 'descend',
        // sorter: (a, b) => {
        //     console.log(a)
        //     console.log(b)
        // },
    },
    {
        title: 'Address',
        dataIndex: 'address',
        onFilter: (value, record) => record.address.indexOf(value as string) === 0,
    },
];

const data = [
    {
        key: '1',
        name: 'John Brown',
        age: 32,
        address: 'New York No. 1 Lake Park',
    },
    {
        key: '2',
        name: 'Jim Green',
        age: 42,
        address: 'London No. 1 Lake Park',
    },
    {
        key: '3',
        name: 'Joe Black',
        age: 32,
        address: 'Sydney No. 1 Lake Park',
    },
    {
        key: '4',
        name: 'Jim Red',
        age: 32,
        address: 'London No. 2 Lake Park',
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

                </div>
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