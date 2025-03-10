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
package com.fuhouyu.sass.platform.system.domain.dto.permission;

import com.fuhouyu.sass.platform.system.domain.dto.page.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 权限查询dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/3 22:32
 */
@Schema(name = "PermissionPageQueryDTO", description = "权限查询dto对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionPageQueryDTO extends PageQueryDTO {

    @Schema(name = "parentId", description = "父级id")
    private Long parentId;

    @Schema(name = "permissionName", description = "权限名称筛选")
    private String permissionName;

    public PermissionPageQueryDTO() {
        this.parentId = -1L;
        this.setSortColumn("display_order");
        this.setIsAsc(true);
    }
}
