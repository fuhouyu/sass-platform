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

export interface OnlineUser {
    // 访问令牌, 仅在返回时填充该值
    accessToken?: string;
    // 登录时间
    loginTime?: Date;
    // 登录账号
    loginAccount?: string;
    // 登录ip
    loginIp?: string;
    // 登录地点
    loginLocation?: string;
    // 登录的租户id
    loginTenantId?: number;
    // 登录类型
    loginType?: string;
    // 登录的客户端id，如果登录的租户后台，则该值为null
    clientId?: string | null;
    // 操作系统
    os?: string;
    // 浏览器
    browser?: string;
    // 浏览器版本
    browserVersion?: string;
    // engine
    engine?: string;
    // 平台
    platform?: string;
}
