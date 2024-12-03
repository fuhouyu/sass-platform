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
import {TableColumnsType} from "antd";
import {PageQuery, PageResult} from "@/model/pageQuery";
import {AnyObject} from "antd/es/_util/type";


// 搜索组件的主接口
export interface TableProps {
    /**
     * 名称
     */
    tableName: string;
    /**
     * table的列
     */
    columns: TableColumnsType;
    /**
     * 分页数据
     */
    pageData?: PageResult<AnyObject>;
    /**
     * 设置分页查询
     */
    setPageQuery?: (pageQuery: PageQuery) => void;
    /**
     * 多选时的回调
     * @param rowKey 每行的key
     */
    setMultipleChooseRowKey?: (rowKey: React.Key[]) => void;
    /**
     * 组件数组
     */
    components?: React.ReactNode[];

}
