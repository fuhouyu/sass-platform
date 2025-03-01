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
package com.fuhouyu.sass.platform.system.dto.organization;

import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 组织分页查询的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/4 22:44
 */
@Schema(name = "OrganizationPageQueryDTO", description = "组织分页查询的dto对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class OrganizationPageQueryDTO extends PageQueryDTO {

    @Schema(name = "parentId", description = "父级id")
    private Long parentId;

    @Schema(name = "organizationName", description = "组织名称模糊查询")
    private String organizationName;

    public OrganizationPageQueryDTO() {
        this.parentId = -1L;
        this.setSortColumn("display_order");
        this.setIsAsc(true);
    }
}
