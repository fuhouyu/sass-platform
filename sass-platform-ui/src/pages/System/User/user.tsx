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


import React, {useEffect, useState} from "react";
import {Button, Input, message, Table, TableColumnsType, TableProps} from "antd";
import "./index.scss"
import {PageQuery, PageResult} from "@/model/page";
import {getUserListApi} from "@/apis/user";
import {UserinfoInterface} from "@/model/user";
import {SearchOutlined} from "@ant-design/icons";
import {SorterResult, TablePaginationConfig} from "antd/es/table/interface";


const User: React.FC = () => {
    const [userPageQuery, setUserPageQuery] = useState<PageQuery>({
        pageNum: 1,
        pageSize: 10,
    });

    const [keyword, setKeyword] = useState<string>('');
    const [loading, setLoading] = useState(false);
    const [pageResult, setPageResult] = useState<PageResult<UserinfoInterface>>({});

    const columns: TableColumnsType = [
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

    ];

    const getUserList = (userPageQuery: PageQuery) => {
        setLoading(true)
        getUserListApi(userPageQuery)
            .then((res: PageResult<UserinfoInterface>) => {
                setPageResult({
                    pageNum: res.pageNum,
                    pageSize: res.pageSize,
                    list: res.list,
                });
                setLoading(false)
            }).catch((err: Error) => {
            message.error(err.message);
        })
    }

    const camelToSnake = (str: string): string => {
        return str.replace(/[A-Z]/g, (letter: string) => `_${letter.toLowerCase()}`);
    };

    const onChange: TableProps['onChange'] = (pagination: TablePaginationConfig, sorter: SorterResult) => {
        console.log(sorter)
        setUserPageQuery({
            pageNum: pagination.current,
            pageSize: pagination.pageSize,
            sortColumn: camelToSnake(sorter.field.toString),
            direction: sorter.order?.replace("end", "").toUpperCase()
            // direction: sorter.
        })
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            setUserPageQuery({keyword})
        }
    };

    useEffect(() => {
        getUserList(userPageQuery);
    }, [userPageQuery])


    return (
        <>
            <div className="user-container">
                <div className="search-header">
                    <div className="search-info">
                        <span>关键字查询</span>
                        <Input
                            value={keyword}
                            onKeyDown={handleKeyDown}
                            placeholder="请输入用户关键字"
                            onChange={(e) => setKeyword(e.target.value)}/>
                    </div>
                    <div className="search-submit">
                        <Button type="primary" icon={<SearchOutlined/>}
                                onClick={() => setUserPageQuery({keyword})}>搜索</Button>
                    </div>
                </div>
                <div className="divide"/>
                <div className="user-list">
                    <Table
                        columns={columns}
                        rowKey="id"
                        dataSource={pageResult.list}
                        onChange={onChange}
                        loading={loading}
                        pagination={{
                            total: pageResult.total,
                            hideOnSinglePage: false,
                            showSizeChanger: true,
                            defaultPageSize: userPageQuery.pageSize,
                            locale: {items_per_page: '条/页'}
                        }}
                        showSorterTooltip={{target: 'sorter-icon'}}
                    />
                </div>
            </div>
        </>
    )
}

export default User;