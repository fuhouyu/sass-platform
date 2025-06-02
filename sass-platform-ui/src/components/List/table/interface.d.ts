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

import {ReactNode, Ref} from "react";
import {TableProps as AntdTableProps} from "antd";
import {IPageQuery, IPageResult} from "@/types/pageQuery";
import {AnyObject} from "antd/es/_util/type";

export type TableRefType<T> = {
    /**
     * 刷新列表
     */
    refreshPageList: (refreshProps?: RefreshPageProps<T>) => Promise<void>;

    /**
     * 列表
     */
    pageResult: IPageResult<T> | undefined

};


// 搜索组件的主接口
export interface TableProps<RecordType = AnyObject> extends AntdTableProps<RecordType> {
    /**
     * ref
     */
    tableRef?: Ref<TableRefType<RecordType>> | undefined
    /**
     * 名称
     */
    tableName?: string;

    /**
     * 分页查询api
     * @param pageQuery 查询参数
     */
    pageApi: (pageQuery: IPageQuery) => Promise<IPageResult<RecordType>>

    /**
     * 行主键
     */
    rowKey?: string;

    /**
     * 组件数组
     */
    tableComponents?: ReactNode[];

    /**
     * 关闭表格提示
     */
    disableTableHint?: boolean;
}

export interface RefreshPageProps<T> {

    /**
     * 处理pageData
     * @param pageData pageData
     */
    dataCallback?: (pageData: IPageResult<T>) => void

    /**
     * 分页查询对象
     */
    pageQuery?: Record<string, string | undefined> | IPageQuery;
}
