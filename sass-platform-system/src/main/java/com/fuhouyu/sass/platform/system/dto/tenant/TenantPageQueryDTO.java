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
package com.fuhouyu.sass.platform.system.dto.tenant;

import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 租户分页查询的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/1 16:15
 */
@Schema(name = "TenantPageQueryDTO", description = "租户分页查询的dto对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantPageQueryDTO extends PageQueryDTO {

    @Schema(name = "tenantName", description = "租户名称模糊查询")
    private String tenantName;
}
