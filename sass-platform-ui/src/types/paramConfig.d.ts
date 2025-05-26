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

import {BaseModel} from "@/types/base";

export interface IParamConfig extends BaseModel {
    /**
     * 主键id
     */
    id?: string;

    /**
     * 配置名称
     */
    configName?: string;

    /**
     * 配置键
     */
    configKey?: string;

    /**
     * 配置value
     */
    configValue?: string;

    /**
     * 分组标识
     */
    groupKey?: string;

    /**
     * 备注
     */
    remark?: string;

    /**
     * 是否允许修改
     */
    isAllowModified?: boolean;

}
