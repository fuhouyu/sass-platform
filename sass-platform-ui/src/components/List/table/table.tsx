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
import {Flex, Space, Table as AntdTable, TableProps as AntdTableProps} from "antd";
import {RefreshPageProps, TableProps} from "@components/List/table/interface";
import {FilterValue, SorterResult, TablePaginationConfig} from "antd/es/table/interface";
import './index.scss'
import {InfoCircleFilled} from "@ant-design/icons";
import {useTranslation} from "react-i18next";
import {useCallback, useEffect, useImperativeHandle, useRef, useState} from "react";
import useRouteSearchParams from "@/hooks/useRouteSearchParams.tsx";
import {useLocation, useSearchParams} from "react-router-dom";
import {PageQuery, PageResult} from "@/model/pageQuery.tsx";

/**
 * 处理_转换为驼峰
 * @param str 下划线字符
 */
const camelToSnake = (str: string | undefined): string | undefined => {
    if (!str) return str;
    return str.replace(/[A-Z]/g, (letter: string) => `_${letter.toLowerCase()}`);
};

const initPageQuery: PageQuery = {
    pageNum: 1,
    pageSize: 10
}


const Table = <T extends object>(tableProps: TableProps<T>) => {
    const {pageApi, tableName, tableRef, tableComponents, disableTableHint} = tableProps;
    const {t} = useTranslation();
    const [pageResult, setPageResult] = useState<PageResult<T>>()
    const [searchParams] = useSearchParams();
    const {updateSearchParams} = useRouteSearchParams();
    const location = useLocation();

    const containerRef = useRef<HTMLDivElement>(null);
    const [scrollY, setScrollY] = useState<number>(500); // 默认初始值

    useEffect(() => {
        const calcScrollHeight = () => {
            if (!containerRef.current) return;

            const windowHeight = window.innerHeight;
            const containerTop = containerRef.current.getBoundingClientRect().top;

            // 保留 5% 间距：windowHeight * 0.05
            const maxTableHeight = windowHeight - containerTop - windowHeight * 0.04;

            setScrollY(maxTableHeight);
        };

        // 初次加载和窗口变化时重新计算
        calcScrollHeight();
        window.addEventListener('resize', calcScrollHeight);
        return () => window.removeEventListener('resize', calcScrollHeight);
    }, []);


    /**
     * 刷新页面
     */
    const refreshPageList = useCallback(async (refreshProps?: RefreshPageProps<T>) => {
        const currentParams = Object.fromEntries(searchParams.entries());
        const mergedParams = {...initPageQuery, ...currentParams, ...refreshProps?.pageQuery};
        const res = await pageApi(mergedParams);
        refreshProps?.dataCallback?.(res);
        setPageResult({...res})
    }, [pageApi, searchParams]);

    useImperativeHandle(tableRef, () => ({
        refreshPageList: async (refreshProps?: RefreshPageProps<T>) => {
            await refreshPageList(refreshProps);
        },
        pageResult: pageResult,
    }));

    useEffect(() => {
        refreshPageList().then();
    }, [refreshPageList, location.key])

    /**
     * change 事件
     * @param pagination 分页
     * @param _ 过滤，暂不使用
     * @param sorters 排序
     */
    const onChange: AntdTableProps['onChange'] = (pagination: TablePaginationConfig, _: Record<string, FilterValue | null>, sorters: SorterResult | SorterResult[]) => {
        const sorter = Array.isArray(sorters) ? sorters[0] : sorters;
        let isAsc = true;
        if (sorter.order) {
            isAsc = sorter.order.toLowerCase() === 'ascend';
        }
        const pageQuery = {
            pageNum: pagination.current,
            pageSize: pagination.pageSize,
            sortColumn: camelToSnake(sorter?.field?.toLocaleString()),
            isAsc: isAsc,
        }
        updateSearchParams(pageQuery)
    };

    return (
        <>
            <Flex className="table-container" vertical ref={containerRef}>
                <div className="title-line">
                    {tableName &&
                        <span className="title">
                            {tableName}
                        </span>}
                    <div className="components">
                        {tableComponents?.map((component, index) => (
                            <div className='component' key={index}>
                                {component}
                            </div>
                        ))
                        }
                    </div>
                </div>
                {!disableTableHint && <div className="tips-container">
                    <Space>
                        <InfoCircleFilled className="table-tips-icon"/>
                        <span>{t('Common.listTips')}</span>
                    </Space>
                </div>}
                <AntdTable
                    {...tableProps}
                    size={'middle'}
                    rowKey={tableProps.rowKey ?? 'id'}
                    onChange={onChange}
                    scroll={{y: scrollY}}

                    dataSource={pageResult?.list}
                    pagination={{
                        defaultCurrent: (searchParams.get('pageNum') ?? 1) as number,
                        total: pageResult?.total,
                        hideOnSinglePage: false,
                        showSizeChanger: true,
                        defaultPageSize: pageResult?.pageSize ?? 10,
                    }}
                    showSorterTooltip={{target: 'sorter-icon'}}
                />
            </Flex>
        </>
    )
}

export default Table;