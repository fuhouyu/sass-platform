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


import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.ts";
import {request} from "@/utils";
import {TenantSpace} from "@/model/tenant.tsx";

class TenantSpaceApi {
    _baseUrl: string;

    constructor(baseUrl: string) {
        this._baseUrl = baseUrl;
    }

    /**
     * 检查租户桶名是否存在
     * @param bucketName 桶名
     */
    checkSpaceNameExists: (bucketName: string) => Promise<boolean> = (bucketName: string): Promise<boolean> => request.get(`${this._baseUrl}/exists?bucketName=${bucketName}`)

    /**
     * 通过租户id获取租户空间
     * @param tenantId 租户id
     */
    getTenantSpaceByTenantId: (tenantId: string) => Promise<TenantSpace> = (tenantId: string): Promise<TenantSpace> => request.get(`${this._baseUrl}/${tenantId}`);

    /**
     * 获取当前登录用户租户空间
     */
    getTenantSpaceForMe: () => Promise<TenantSpace> = (): Promise<TenantSpace> => request.get(`${this._baseUrl}/me`);

}


export const tenantSpaceApi = new TenantSpaceApi(BaseApiUrlConstant.TENANT_SPACE_API_PREFIX);
