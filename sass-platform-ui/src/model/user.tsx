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

import {BaseModel} from "@/model/base";
import {Account} from "@/model/account.tsx";

export interface UserPosition extends BaseModel {
    /**
     * 用户id
     */
    userId?: string;

    /**
     * 组织id
     */
    organizationId?: string;

    /**
     * 职位名称
     */
    positionName?: string;

    /**
     * 是否主职
     */
    isMain?: boolean;


}
/**
 * 用户详情
 */
export interface Userinfo extends BaseModel {
    /**
     * 头像地址
     */
    avatar?: string;
    /**
     * 邮件地址
     */
    email?: string;
    /**
     * 性别
     */
    gender?: string;
    /**
     * 主键id
     */
    id?: string;
    /**
     * 登录时间
     */
    loginDate?: string;
    /**
     * 登录ip
     */
    loginIp?: string;
    /**
     * 真实姓名
     */
    realName?: string;
    /**
     * 昵称
     */
    nickname?: string;
    /**
     * 用户名
     */
    username?: string;

    /**
     * 用户职位
     */
    userPosition?: UserPosition;

    /**
     * 用户账号
     */
    account?: Account;

    /**
     租户id
     */
    tenantId: string;
}