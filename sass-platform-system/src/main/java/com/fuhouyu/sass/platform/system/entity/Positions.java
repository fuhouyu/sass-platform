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

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 岗位
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/8 21:13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Positions extends BaseEntity {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 岗位编码
     */
    private String positionCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 租户id
     */
    private Long ownerTenantId;
}
