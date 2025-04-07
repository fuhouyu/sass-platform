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
package com.fuhouyu.sass.platform.system.domain.dto.tenant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Set;

/**
 * <p>
 * 新增或修改租户的详情dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/5 14:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SaveOrEditTenantInfoDTO extends TenantInfoDTO {

    @Serial
    private static final long serialVersionUID = 8971283681752371653L;

    @Schema(name = "addPermissionIds", description = "需要添加的权限id")
    private Set<Long> addPermissionIds;

    @Schema(name = "deletePermissionIds", description = "需要删除的权限id")
    private Set<Long> deletePermissionIds;

    @Schema(name = "tenantSpace", description = "租户空间信息")
    @Valid
    private TenantSpaceDTO tenantSpace;

    public SaveOrEditTenantInfoDTO() {
        setTenantType("COMPANY");
    }
}
