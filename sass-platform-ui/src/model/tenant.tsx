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
import {Key} from "react";

/**
 * 租户详情
 */
export interface TenantInfo extends BaseModel {
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
    // 是否启用
    isEnabled?: boolean;
    // 后端返回的权限ids
    permissionIds?: Key[];
    // 要添加的权限ids
    addPermissionIds?: Key[];
    // 要被删除的权限ids
    deletePermissionIds?: Key[];
    // 管理员用户真实姓名
    adminUserRealName?: string;
    // 租户空间
    tenantSpace?: TenantSpace;
    // 开始日期
    startDate?: string;
    // 结束日期
    endDate?: string
}


export interface TenantSpace extends BaseModel {

    /**
     * 租户名称
     */
    tenant: string;

    /**
     * 桶名称
     */
    bucketName: string;

    /**
     * acl
     */
    acl: 'private' | 'public-read' | 'public-read-write' | 'authenticated-read';

    /**
     * 容量
     */
    capacity: number;

    /**
     * 已用容量
     */
    usedCapacity: number;
}