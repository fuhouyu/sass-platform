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

import java.io.Serial;

/**
 * <p>
 * 参数配置实体
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/2 22:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ParamConfigs extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 897123786521736213L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置key
     */
    private String configKey;

    /**
     * 配置value
     */
    private String configValue;

    /**
     * 分组标识
     */
    private String groupKey;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否允许修改
     */
    private Boolean isAllowModified;

}
