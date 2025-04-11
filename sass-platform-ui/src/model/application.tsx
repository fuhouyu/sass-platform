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

import {BaseModel} from "@/model/base.tsx";

/**
 * 应用
 *
 */
export interface Application extends BaseModel {
    // 客户端id
    clientId?: string;
    // 客户端密钥
    clientSecret?: string;
    // 客户端名称
    clientName?: string;
    // icon
    icon?: number;
    // 备注
    remark?: string;
    // 回调url
    redirectUris?: string[];
    // scopes
    scopes?: string[];
    // 授权类型
    grantTypes?: string[];
    // 认证令牌有效期
    accessTokenValidity?: number;
    // 刷新令牌有效期
    refreshTokenValidity?: number;
    // 是否上架
    published?: boolean;
    // ip白名单
    ipWhitelist?: string[];
    // 是否启用
    isEnabled?: boolean;
}