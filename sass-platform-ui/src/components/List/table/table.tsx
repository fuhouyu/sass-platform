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
import {Table as _Table, TableProps as _TableProps} from "antd";
import {TableProps} from "@components/List/table/interface";
import {IconFont} from "@/components";
import {FilterValue, SorterResult, TablePaginationConfig} from "antd/es/table/interface";
import './index.scss'

/**
 * 处理_转换为驼峰
 * @param str 下划线字符
 */
const camelToSnake = (str: string | undefined): string | undefined => {
    if (!str) return str;
    return str.replace(/[A-Z]/g, (letter: string) => `_${letter.toLowerCase()}`);
};

const Table = <T extends object>(tableProps: TableProps<T>) => {
    const {setPageQuery, rowKey, rowSelection, tableName, pageData, columns, components} = tableProps;

    /**
     * change 事件
     * @param pagination 分页
     * @param _ 过滤，暂不使用
     * @param sorters 排序
     */
    const onChange: _TableProps['onChange'] = (pagination: TablePaginationConfig, _: Record<string, FilterValue | null>, sorters: SorterResult | SorterResult[]) => {
        if (!setPageQuery) {
            return
        }
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

    return (
        <>
            <div className="table-container">
                <div className="title-line">
                        <span className="title">
                            {tableName}
                        </span>
                    <div className="components">
                        {components?.map((component, index) => (
                            <div className='component' key={index}>
                                {component}
                            </div>
                        ))
                        }
                    </div>
                </div>
                <div className="tips-container">
                    <IconFont className='tips' type="i-tips"/>
                    <span>选择列表数据后可进行批量操作</span>
                </div>
            </div>
            <div className="list">
                <_Table
                    rowSelection={rowSelection}
                    scroll={{x: '100%'}}
                    columns={columns}
                    style={{tableLayout: 'fixed'}}
                    rowKey={rowKey ?? "id"}
                    dataSource={pageData?.list}
                    onChange={onChange}
                    pagination={{
                        total: pageData?.total,
                        hideOnSinglePage: false,
                        showSizeChanger: true,
                        defaultPageSize: pageData?.pageSize ?? 10,
                        locale: {items_per_page: '条/页'}
                    }}
                    showSorterTooltip={{target: 'sorter-icon'}}
                />
            </div>
        </>
    )
}

export default Table;