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
import {TenantConfig} from "@/model/tenant";

const baseTenantUrl = '/v1/tenant-config'

/**
 * 租户配置接口
 * @param pageQuery 分页查询对象
 */
const getTenantConfigListApi = <P extends PageQuery, R extends object>(pageQuery: P): Promise<PageResult<R>> =>
    request.get(`${baseTenantUrl}/list`, {
        params: {...pageQuery}
    });

/**
 * 通过id获取a详情
 * @param id 主键id
 */
const getTenantConfigApi = (id: string): Promise<TenantConfig> =>
    request.get(`${baseTenantUrl}/${id}`)

/**
 * 保存租户配置api
 * @param tenantConfig 租户配置
 */
const saveTenantConfigApi = (tenantConfig: TenantConfig): Promise<string> =>
    request.post(`${baseTenantUrl}`, tenantConfig);

/**
 * 修改租户配置
 * @param updateId 修改的id
 * @param tenantConfig 租户配置
 */
const updateTenantConfigApi = (updateId: string, tenantConfig: TenantConfig): Promise<void> =>
    request.put(`${baseTenantUrl}/${updateId}`, tenantConfig);

/**
 * 删除租户配置的api
 * @param ids ids
 */
const removerTenantConfigApi = (ids: string[]): Promise<void> => request.delete(`${baseTenantUrl}`, {data: ids});

export {
    getTenantConfigListApi,
    removerTenantConfigApi,
    saveTenantConfigApi,
    getTenantConfigApi,
    updateTenantConfigApi
}