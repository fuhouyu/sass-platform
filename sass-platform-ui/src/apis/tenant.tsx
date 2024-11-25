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

import {PageQuery, PageResult} from "@/model/pageQuery";
import {request} from "@/utils";
import {TenantInfo} from "@/model/tenant";

const baseTenantUrl = '/v1/tenant'

/**
 * 分页获取租户列表
 * @param pageQuery 分页查询
 */
const getTenantListApi = <P extends PageQuery, R extends object>(pageQuery: P): Promise<PageResult<R>> =>
    request.get(`${baseTenantUrl}/list`, {
        params: {...pageQuery}
    });

/**
 * 保存租户
 * @param tenantInfo 租户详情
 */
const saveTenantApi: (tenantInfo: TenantInfo) => Promise<number> = (tenantInfo: TenantInfo) => request.post(`${baseTenantUrl}`, tenantInfo);

/**
 * 修改租户
 * @param id 主键id
 * @param tenantInfo 租户详情
 */
const updateTenantApi: (id: string, tenantInfo: TenantInfo) => Promise<void> = (id: string, tenantInfo: TenantInfo) => request.put(`${baseTenantUrl}/${id}`, tenantInfo);

/**
 * 通过id获取详情
 * @param id 主键id
 */
const getTenantInfoApi: (id: string) => Promise<TenantInfo> = (id: string): Promise<TenantInfo> => request.get(`${baseTenantUrl}/${id}`);

/**
 * 通过id删除租户
 * @param ids 用户集合
 *
 */
const removeTenantApi = (ids: string[]): Promise<void> => request.delete(`${baseTenantUrl}`, {
    data: ids
});

export {
    getTenantListApi,
    updateTenantApi,
    getTenantInfoApi,
    saveTenantApi,
    removeTenantApi
}