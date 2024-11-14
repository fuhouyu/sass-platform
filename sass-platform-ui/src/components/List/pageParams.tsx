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
import {Input, Select, TableColumnsType} from "antd";
import {PageQuery, PageResult} from "@/model/page";
import {DefaultOptionType} from "rc-select/lib/Select";

/**
 * 搜索组件props
 */
type SearchCommentProps = {
    key: string
    // 仅适于用options组件
    options?: DefaultOptionType[]
    value: string | undefined;
    onKeyDown: React.KeyboardEventHandler<HTMLInputElement>;
    placeholder: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
};
/**
 * 搜索组件
 */
export type SearchComment = {
    key: string
    name: string
    placeholder?: string
    options?: DefaultOptionType[]
    comment: React.ComponentType<SearchCommentProps>;
}


// input组件
export const SearchInput = Input as React.ComponentType<SearchCommentProps>;
// select 组件
export const SearchSelection = Select as React.ComponentType<SearchCommentProps>;


/**
 * 分页参数
 */
export interface PageListParams {
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
    searchComments?: SearchComment[]
    /**
     * ref
     */
    ref?: React.Ref<PageListHandler>
}

/**
 * 分页处理器
 */
export type PageListHandler = {
    refresh: () => void;
} | undefined;
