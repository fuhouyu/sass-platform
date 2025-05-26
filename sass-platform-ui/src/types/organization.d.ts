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

export interface IOrganization extends BaseModel {

    /**
     * 主键id
     */
    id: string;

    /**
     * 父级id
     */
    parentId: string;

    /**
     * 组织名称
     */
    organizationName: string;

    /**
     * 组织编码
     */
    organizationCode: string;

    /**
     * 组织类型
     */
    organizationType: string;

    /**
     * 是否启用
     */
    isEnabled: boolean;

    /**
     * 是否为叶子节点
     */
    isLeaf: boolean;

    /**
     * 备注
     */
    remark: string;

    /**
     * 显示顺序
     */
    displayOrder: number;

    /**
     * 子集
     */
    children?: IOrganization[]
}
