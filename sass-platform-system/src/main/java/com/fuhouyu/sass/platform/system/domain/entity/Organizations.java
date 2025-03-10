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
package com.fuhouyu.sass.platform.system.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 组织实体
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/4 21:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Organizations extends BaseEntity {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 主键id
     */
    private Long parentId;

    /**
     * 组织名称
     */
    private String organizationName;

    /**
     * 组织编码
     */
    private String organizationCode;

    /**
     * 组织类型
     */
    private String organizationType;

    /**
     * 启禁用状态
     */
    private Boolean isEnabled;

    /**
     * 是否叶子节点
     */
    private Boolean isLeaf;

    /**
     * 备注
     */
    private String remark;

    /**
     * 显示顺序
     */
    private Integer displayOrder;

    /**
     * 组织id
     */
    private Long ownerTenantId;
}
