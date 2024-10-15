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


import {Button, Input, message, Table, TableColumnsType, TableProps} from "antd";
import {PageQuery, PageResult} from "@/model/page";
import React, {useEffect, useState} from "react";
import {SearchOutlined} from "@ant-design/icons";
import {IconFont} from "@components/Iconfont/iconfont";
import {SorterResult, TablePaginationConfig} from "antd/es/table/interface";
import {UserinfoInterface} from "@/model/user";
import './index.scss'

export interface PageListInterface {
    /**
     * 列表名称
     */
    listName: string;
    columns: TableColumnsType;

}

/**
 * 处理_转换为驼峰
 * @param str 下划线字符
 */
const camelToSnake = (str: string | undefined): string | undefined => {
    if (!str) return str;
    return str.replace(/[A-Z]/g, (letter: string) => `_${letter.toLowerCase()}`);
};

const PageList: React.FC<{
    pageListInterface: PageListInterface,
    pageRequestApi: <R extends object>(pageQuery: PageQuery) => Promise<PageResult<R>>
    deleteButtonApi: (ids: React.Key[]) => Promise<void>
}> = ({pageListInterface, pageRequestApi, deleteButtonApi}) => {
    const [pageQuery, setPageQuery] = useState<PageQuery>({
        pageNum: 1,
        pageSize: 10,
    });

    const [keyword, setKeyword] = useState<string>('');
    const [loading, setLoading] = useState(false);
    const [pageResult, setPageResult] = useState<PageResult<UserinfoInterface>>({});
    const [deleteIds, setDeleteIds] = useState<React.Key[]>([]);

    const rowSelection: TableProps['rowSelection'] = {
        onChange: (selectedRowKeys: React.Key[]) => {
            if (selectedRowKeys === undefined || selectedRowKeys.length === 0) {
                setDeleteIds([]);
                return;
            }
            setDeleteIds(selectedRowKeys);
        },
    };

    useEffect(() => {
        setLoading(true);
        pageRequestApi(pageQuery)
            .then((pageResult: PageResult<UserinfoInterface>) => {
                setPageResult({...pageResult});
            })
        setLoading(false);
    }, [pageQuery])


    const onChange: TableProps['onChange'] = (pagination: TablePaginationConfig, sorter: SorterResult) => {
        console.log(sorter)
        setPageQuery({
            pageNum: pagination.current,
            pageSize: pagination.pageSize,
            sortColumn: camelToSnake(sorter?.field?.toLocaleString()),
            direction: sorter.order?.replace("end", "").toUpperCase()
        })
    };

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            setPageQuery({keyword})
        }
    };

    const onDeleteButtonClick = () => {
        deleteButtonApi(deleteIds)
            .then(() => {
                message.success("删除成功");
                setDeleteIds([]);
            })
            .catch((error: Error) => {
                message.error(error.message);
            })
    }
    return (
        <div className="list-container">
            <div className="search-header">
                <div className="search-info">
                    <span>关键字查询</span>
                    <Input
                        value={keyword}
                        onKeyDown={handleKeyDown}
                        placeholder="请输入关键字查询"
                        onChange={(e) => setKeyword(e.target.value)}/>
                </div>
                <div className="search-submit">
                    <Button type="primary" icon={<SearchOutlined/>}
                            onClick={() => setPageQuery({keyword})}>搜索</Button>
                </div>
            </div>
            <div className="table-container">
                <div className="title-container">
                    <div className="title-line">
                        <div className="title">
                            {pageListInterface.listName}列表
                        </div>
                        <div className="buttons">
                            <Button icon={<IconFont type="i-add" style={{color: 'white'}}/>}>
                                新增
                            </Button>
                            <Button disabled={deleteIds.length === 0}
                                    onClick={onDeleteButtonClick}
                                    icon={<IconFont type="i-delete" style={{color: 'white'}}/>}>
                                删除
                            </Button>
                        </div>
                    </div>
                    <div className="checked-num">
                        <IconFont type="i-tips" style={{color: 'white'}}/>
                        选择列表数据后可进行批量操作
                    </div>
                </div>
                <div className="list">
                    <Table
                        rowSelection={{type: 'checkbox', ...rowSelection}}
                        scroll={{x: '100%'}}
                        columns={pageListInterface.columns}
                        style={{tableLayout: 'fixed'}}
                        rowKey="id"
                        dataSource={pageResult.list}
                        onChange={onChange}
                        loading={loading}
                        pagination={{
                            total: pageResult.total,
                            hideOnSinglePage: false,
                            showSizeChanger: true,
                            defaultPageSize: pageQuery.pageSize,
                            locale: {items_per_page: '条/页'}
                        }}
                        showSorterTooltip={{target: 'sorter-icon'}}
                    />
                </div>
            </div>
        </div>
    )
}

export {
    PageList
}