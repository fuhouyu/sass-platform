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


import {PageQuery} from "@/model/pageQuery";
import {SearchComponentProps} from "@components/List/header/interface.d";
import {TableProps} from "@components/List/table/index.d";


/**
 * 分页属性
 */
export interface PageListProps {
    /**
     * table 属性
     */
    tableProps: TableProps;
    /**
     * 头搜索组件
     */
    headerSearchProps?: SearchComponentProps;
    /**
     * 设置分页查询
     * @param pageQuery 查询对象
     */
    setPageQuery?: (pageQuery: PageQuery) => void;
}

/**
 * 分页处理器
 */
export type PageListHandler = {
    setSearchValue: (key: string, value: unknown) => void;
    refresh: () => void;
} | undefined;
