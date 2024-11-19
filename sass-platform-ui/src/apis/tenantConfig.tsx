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


import {PageQueryModel, PageResultModel} from "@/model/page";
import {request} from "@/utils";

const baseTenantUrl = '/v1/tenant-config'

/**
 * 租户配置接口
 * @param pageQuery 分页查询对象
 */
const getTenantConfigListApi = <P extends PageQueryModel, R extends object>(pageQuery: P): Promise<PageResultModel<R>> =>
    request.get(`${baseTenantUrl}/list`, {
        params: {...pageQuery}
    });


/**
 * 删除租户配置的api
 * @param ids ids
 */
const removerTenantConfigApi = (ids: string[]): Promise<void> => request.delete(`${baseTenantUrl}`, {data: ids});

export {
    getTenantConfigListApi,
    removerTenantConfigApi
}