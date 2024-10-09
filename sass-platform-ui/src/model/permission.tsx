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


export interface PermissionInterface {
    /**
     * 权限树子集
     */
    children?: Response[];
    /**
     * 组件路径
     */
    componentPath?: string;
    /**
     * 创建人
     */
    createAt: string;
    /**
     * 创建时间
     */
    createTime: Date;
    /**
     * 显示顺序
     */
    displayOrder: number;
    /**
     * icon
     */
    icon?: string;
    /**
     * 主键id
     */
    id: number;
    /**
     * 是否允许修改
     */
    isAllowModified: boolean;
    /**
     * 是否外链
     */
    isFrame: boolean;
    /**
     * 是否系统权限
     */
    isSystemd: boolean;
    /**
     * 是否显示
     */
    isVisible: boolean;
    /**
     * 父级id，一级时为-1
     */
    parentId: number;
    /**
     * 权限编码
     */
    permissionCode: string;
    /**
     * 权限名称
     */
    permissionName: string;
    /**
     * 路由路径
     */
    routePath?: string;
    /**
     * 操作人
     */
    updateAt: string;
    /**
     * 更新时间
     */
    updateTime: Date;
    /**
     * url参数
     */
    urlParams?: string;
}