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
package com.fuhouyu.sass.platform.system.domain.dto.role;

import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import com.fuhouyu.sass.platform.system.domain.entity.Roles;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色查询的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/11 21:15
 */
@Schema(name = "RolePageQueryDTO", description = "角色查询的dto对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class RolePageQueryDTO extends PageQueryDTO<Roles> {

    @Schema(name = "roleCode", description = "角色编码检索")
    private String roleCode;

    @Schema(name = "isEnabled", description = "是否启禁用筛选")
    private Boolean isEnabled;
}
