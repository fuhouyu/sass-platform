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
package com.fuhouyu.sass.platform.system.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * <p>
 * 权限do对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 17:58
 */
@Getter
@Setter
@ToString(callSuper = true)
public class Permissions extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1123897912371561223L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限编码
     */
    private String permissionCode;

    /**
     * 权限类型
     */
    private String permissionType;

    /**
     * 排序顺序
     */
    private Integer displayOrder;

    /**
     * icon
     */
    private String icon;

    /**
     * 路由路径
     */
    private String routePath;

    /**
     * 组件路径
     */
    private String componentPath;

    /**
     * url参数
     */
    private String urlParams;

    /**
     * 是否frame
     */
    private Boolean isFrame;

    /**
     * 是否允许修改
     */
    private Boolean isAllowModified;

    /**
     * 是否显示
     */
    private Boolean isVisible;

    /**
     * 租户id
     */
    private Long ownerTenantId;

    /**
     * 是否为叶子节点
     */
    private Boolean isLeaf;
}
