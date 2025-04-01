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

import {PageQuery} from "@/model/pageQuery.tsx";

/**
 * 操作日志分页查询对象
 */
export interface OperationLogPageQuery extends PageQuery {
    systemName?: string;
    moduleName?: string;
    risk?: string;
    isSuccess?: boolean;
}


export interface OperationLog {
    /**
     * 主键id
     */
    id?: string;

    /**
     * 模块名称
     */
    moduleName?: string;

    /**
     * 请求地址
     */
    requestUri?: string;

    /**
     * 请求ip
     */
    requestIp?: string;

    /**
     * 请求位置
     */
    requestLocation?: string;

    /**
     * 请求方法(GET/POST/PUT/DELETE等)
     */
    requestMethod?: string;

    /**
     * 请求参数(JSON格式)
     */
    requestParam?: string;

    /**
     * 响应数据,isSuccess为false时，这里显示错误信息
     */
    responseData?: string;

    /**
     * 操作类型
     */
    operationType?: string;

    /**
     * 日志内容(中文)
     */
    content?: string;

    /**
     * 日志内容(英文)
     */
    contentEn?: string;

    /**
     * 操作状态(true/false)
     */
    isSuccess?: boolean;

    /**
     * 操作风险类型
     */
    riskType?: string;

    /**
     * 系统名称
     */
    systemName?: string;

    /**
     * 操作人
     */
    operationUser?: string;

    /**
     * 操作时间
     */
    operationTime?: string;

    /**
     * 所属的租户id
     */
    ownerTenantId?: number;
}