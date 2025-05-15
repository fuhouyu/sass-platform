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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 租户dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/17 21:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tenant_space")
public class TenantSpace extends BaseEntity {

    /**
     * 租户id
     */
    @TableId
    private Long tenantId;

    /**
     * 桶名
     */
    private String bucketName;

    /**
     * 存储容量
     */
    private Long capacity;

    /**
     * acl控制权限
     */
    private String acl;
}
