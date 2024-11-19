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

import {BaseModel} from "@/model/base";

/**
 * 租户详情
 */
export interface TenantInfoModel extends BaseModel {
    id?: string;
    // 租户编码
    tenantCode?: string;
    // 租户名称
    tenantName?: string;
    // 租户类型
    tenantType?: string;
    // 备注
    remark?: string;
    // icon
    icon?: string;
    // 联系人
    contactPerson?: string;
    // 联系手机号
    contactInfo?: string;
}


export interface TenantConfigModel extends BaseModel {
    // 配置id
    id?: string;
    // 配置名称
    name?: string;
    // 备注
    remark?: string;
    // 是否启用
    isEnabled?: string;
}