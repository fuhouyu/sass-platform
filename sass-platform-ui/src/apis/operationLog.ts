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

import {IOperationLog, IOperationLogPageQuery} from "@/types/operationLog";
import {IPageResult} from "@/types/pageQuery";
import {request} from "@/utils";
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.ts";

class OperationLogApi {

    private readonly _baseUrl: string;

    constructor(baseUrl: string) {
        this._baseUrl = baseUrl;
    }

    /**
     * 分页查询的对象
     * @param pageQuery 分页查询
     */
    pageApi: (pageQuery: IOperationLogPageQuery) => Promise<IPageResult<IOperationLog>> = (pageQuery: IOperationLogPageQuery): Promise<IPageResult<IOperationLog>> =>
        request.get(`${this._baseUrl}/page`, {
            params: pageQuery
        });

    /**
     * 获取模块列表
     */
    getModuleList: () => Promise<string[]> = (): Promise<string[]> =>
        request.get(`${this._baseUrl}/module-list`);


    /**
     * 操作日志详情
     * @param id 主键id
     */
    operationLogInfo: (id: string) => Promise<IOperationLog> = (id: string): Promise<IOperationLog> =>
        request.get(`${this._baseUrl}/${id}`)
}

export const operationLogApi: OperationLogApi = new OperationLogApi(BaseApiUrlConstant.OPERATION_LOG_URL);
