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
import React from "react";

/**
 * 角色详情
 */
export interface Role extends BaseModel {
    /**
     * 主键id
     */
    id?: string;

    /**
     * 角色名称
     */
    roleName?: string;

    /**
     * 角色编码
     */
    roleCode?: string;

    /**
     * 显示顺序
     */
    displayOrder?: number;

    /**
     * 数据范围
     */
    dataScope?: string;

    /**
     * 状态：true 启用
     */
    isEnabled?: boolean;

    /**
     * 是否允许修改
     */
    isAllowModified?: boolean;

    /**
     * 权限id集合
     */
    permissionIds?: React.Key[]
}