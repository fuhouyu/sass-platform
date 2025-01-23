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

/**
 * 字典类型
 */
export interface DictType extends BaseModel {
    /**
     * 主键id
     */
    id?: string;

    /**
     * 字典名称
     */
    dictName?: string;

    /**
     * 字典编码
     */
    dictCode?: string;

    /**
     * 允许修改
     */
    isAllowModified?: boolean;

    /**
     * 是否启禁用
     */
    isEnabled?: boolean;

    /**
     * 显示
     */
    displayOrder?: number;

    /**
     * 备注
     */
    remark?: string;
}