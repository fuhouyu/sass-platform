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


import {Button, Col, Input, message, Row, Table, TableColumnsType, TableProps} from "antd";
import {PageQuery, PageResult} from "@/model/page";
import React, {forwardRef, KeyboardEventHandler, useCallback, useEffect, useImperativeHandle, useState} from "react";
import {SearchOutlined} from "@ant-design/icons";
import {IconFont} from "@components/Iconfont/iconfont";
import {FilterValue, SorterResult, TablePaginationConfig} from "antd/es/table/interface";
import './index.scss'

/**
 * 搜索
 */
export interface SearchHeaderInterface {
    name: string
    /**
     * 映射值
     */
    value: string
    searchComment: React.ComponentType<{
        value: string | undefined
        onKeyDown: KeyboardEventHandler
        placeholder: string
        onChange: (e: React.ChangeEvent<HTMLInputElement>) => void
    }>;
}

export interface PageListInterface {
    /**
     * 列表名称
     */
    listName: string;
    /**
     * 列名
     */
    columns: TableColumnsType;
    /**
     * 分页查询api接口
     * @param pageQuery 查询api
     */
    pageRequestApi: <R extends object>(pageQuery: PageQuery) => Promise<PageResult<R>>
    /**
     *  新增数据的回调
     */
    addCallback: () => void
    /**
     * 删除数据的回调
     * @param ids 需要删除的ids
     */
    deleteCallback: (ids: string[]) => Promise<void>
    /**
     * 搜索组件
     */
    searchHeaders: SearchHeaderInterface[]
    /**
     * ref
     */
    ref?: React.Ref<PageListHandler>
}

export type PageListHandler = {
    refresh: () => void;
    // 具体的属性和方法定义
} | undefined;

/**
 * 处理_转换为驼峰
 * @param str 下划线字符
 */
const camelToSnake = (str: string | undefined): string | undefined => {
    if (!str) return str;
    return str.replace(/[A-Z]/g, (letter: string) => `_${letter.toLowerCase()}`);
};

const PageList = forwardRef<PageListHandler, PageListInterface>((props, ref) => {
    const {listName, searchHeaders, columns, deleteCallback, addCallback, pageRequestApi} = props
    const [pageQuery, setPageQuery] = useState<PageQuery>({
        pageNum: 1,
        pageSize: 10,
    });


    const [searchValue, setSearchValue] = useState<Record<string, string>>({});
    const [loading, setLoading] = useState(false);
    const [pageResult, setPageResult] = useState<PageResult<object>>();
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

    /**
     * 刷新列表
     * @param pageQuery 分页查询
     */
    const refreshList = useCallback((pageQuery: PageQuery) => {
        pageRequestApi(pageQuery)
            .then((pageResult: PageResult<object>) => {
                setPageResult({...pageResult});
            })
    }, [pageRequestApi, setPageResult])

    useEffect(() => {
        setLoading(true);
        refreshList(pageQuery);
        setLoading(false);
    }, [pageQuery, refreshList])


    useImperativeHandle(ref, () => ({
        refresh: () => {
            pageRequestApi(pageQuery)
                .then((pageResult: PageResult<object>) => {
                    setPageResult({...pageResult});
                })
        },
    }));

    /**
     * change 事件
     * @param pagination 分页
     * @param _ 过滤，暂不使用
     * @param sorters 排序
     */
    const onChange: TableProps['onChange'] = (pagination: TablePaginationConfig, _: Record<string, FilterValue | null>, sorters: SorterResult | SorterResult[]) => {
        const sorter = Array.isArray(sorters) ? sorters[0] : sorters;
        let isAsc = true;
        if (sorter.order) {
            isAsc = sorter.order.toLowerCase() === 'ascend';
        }
        setPageQuery({
            pageNum: pagination.current,
            pageSize: pagination.pageSize,
            sortColumn: camelToSnake(sorter?.field?.toLocaleString()),
            isAsc: isAsc
        });
    };

    /**
     * 处理回车键
     * @param e key事件
     */
    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            setPageQuery({...pageQuery, ...searchValue})
        }
    };

    /**
     * 删除事件
     */
    const onDeleteButtonClick = () => {
        deleteCallback(deleteIds.map(id => String(id)))
            .then(() => {
                message.success("删除成功").then()
                setDeleteIds([]);
            })
            .catch((error: Error) => {
                message.error(error.message).then()
            })
    }

    return (
        <div className="list-container">
            <div className="search-header">
                <Row gutter={10}>
                    <Col>
                        <span>关键字查询</span>
                    </Col>
                    <Col>
                        <Input
                            key={'keyword'}
                            value={searchValue.keyword}
                            onKeyDown={handleKeyDown}
                            placeholder="请输入关键字查询"
                            onChange={(e) => setSearchValue({
                                ...searchValue,
                                keyword: e.target.value
                            })}/>
                    </Col>
                    {searchHeaders.map((searchHeader) =>
                        (
                            <>
                                <Col>
                                    <span>{searchHeader.name}</span>
                                </Col>
                                <Col>
                                    <searchHeader.searchComment
                                        key={searchHeader.value}
                                        value={searchValue[searchHeader.value]}
                                        onKeyDown={handleKeyDown}
                                        placeholder={"请输入" + searchHeader.name}
                                        onChange={(e) => setSearchValue({
                                            ...searchValue,
                                            [searchHeader.value]: e.target.value
                                        })}
                                    >
                                    </searchHeader.searchComment>
                                </Col>
                            </>
                        )
                    )}
                    <Col className="search-button">
                        <Button type="primary" icon={<SearchOutlined/>}
                                onClick={() => setPageQuery({...pageQuery, ...searchValue})}>搜索</Button>
                    </Col>
                </Row>
            </div>
            <div className="table-container">
                <div className="title-container">
                    <div className="title-line">
                        <div className="title">
                            {listName}列表
                        </div>
                        <div className="buttons">
                            <Button className="add-button" onClick={() => addCallback()} icon={<IconFont type="i-add"/>}
                            >
                                新增
                            </Button>
                            <Button className="del-button" disabled={deleteIds.length === 0}
                                    onClick={() => {
                                        onDeleteButtonClick()
                                        refreshList(pageQuery)
                                    }}
                                    icon={<IconFont type="i-delete"/>}>
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
                        columns={columns}
                        style={{tableLayout: 'fixed'}}
                        rowKey="id"
                        dataSource={pageResult?.list}
                        onChange={onChange}
                        loading={loading}
                        pagination={{
                            total: pageResult?.total,
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
    );
})

export {
    PageList
}