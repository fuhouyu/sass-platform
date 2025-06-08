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

import {ITenantInfo} from "@/types/tenant";
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.ts";
import {DefaultApiImpl} from "@/apis/baseApi.ts";
import {request} from "@/utils";


const baseTenantUrl = BaseApiUrlConstant.TENANT_API_PREFIX;

class TenantApi extends DefaultApiImpl<ITenantInfo> {

    constructor() {
        super(baseTenantUrl);
    }

    /**
     * 检查租户编码是否存在
     * true 已存在 false 不存在
     * @param tenantCode 租户编码
     */
    checkTenantCodeExists: (tenantCode: string) => Promise<boolean> = (tenantCode: string): Promise<boolean> => request.get(`${this._baseUrl}/exists?tenantCode=${tenantCode}`)

    /**
     * 查询当前用户所属的租户详情
     */
    findTenantInfoForMe: () => Promise<ITenantInfo> = (): Promise<ITenantInfo> => request.get(`${this._baseUrl}/me`);


    /**
     * 租户列表
     */
    list: () => Promise<ITenantInfo[]> = (): Promise<ITenantInfo[]> => request.get(`${this._baseUrl}/list`);

    /**
     * 重置密码
     * @param id 租户id
     */
    resetPassword: (id: string) => Promise<void> = (id: string): Promise<void> => request.get(`${this._baseUrl}/${id}/reset`);
}

export const tenantApi: TenantApi = new TenantApi()
