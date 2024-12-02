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

/**
 * 分页查询对象
 */
export interface PageQuery {
    pageNum: number | undefined;
    pageSize: number | undefined;
    keyword?: string;
    sortColumn?: string;
    isAsc?: boolean;

    // 允许动态添加任意键
    [key: string]: unknown;
}


/**
 * 分页查询的结果集
 */
export interface PageResult<T> {
    pageNum: number;
    pageSize: number;
    total: number;
    list: T[];
}