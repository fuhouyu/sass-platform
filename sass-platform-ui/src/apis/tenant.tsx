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

import {TenantInfo} from "@/model/tenant";
import {BaseApiUrlConstant} from "@/constants/baseUrlConstant.tsx";
import {DefaultApiImpl} from "@/apis/baseApi";
import {request} from "@/utils";


const baseTenantUrl = BaseApiUrlConstant.TENANT_API_PREFIX;

class TenantApi extends DefaultApiImpl<TenantInfo> {

    constructor() {
        super(baseTenantUrl);
    }

    /**
     * 检查租户编码是否存在
     * true 已存在 false 不存在
     * @param tenantCode 租户编码
     */
    checkTenantCodeExists: (tenantCode: string) => Promise<boolean> = (tenantCode: string): Promise<boolean> => request.get(`${this.baseUrl}/exists?tenantCode=${tenantCode}`)

    /**
     * 查询出租户详情
     */
    findTenantInfoForMe: () => Promise<TenantInfo[]> = (): Promise<TenantInfo[]> => request.get(`${this.baseUrl}/me`);

    /**
     * 切换租户
     * @param tenantId 租户id
     */
    switchTenant: (tenantId: string) => Promise<void> = (tenantId: string): Promise<void> => request.get(`${this.baseUrl}/switch/${tenantId}`);
}

export const tenantApi: TenantApi = new TenantApi()