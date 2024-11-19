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
package com.fuhouyu.sass.platform.system.dto.tenant;

import com.fuhouyu.sass.platform.system.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 租户配置dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/19 21:08
 */
@Schema(name = "TenantConfigDTO", description = "租户配置dto对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantConfigDTO extends BaseDTO {

    @Schema(name = "id", description = "主键id")
    private Long id;

    @Schema(name = "name", description = "配置名称")
    private String name;

    @Schema(name = "remark", description = "备注")
    private String remark;

    @Schema(name = "isEnabled", description = "是否启用：true启用，false禁用")
    private Boolean isEnabled;

}
