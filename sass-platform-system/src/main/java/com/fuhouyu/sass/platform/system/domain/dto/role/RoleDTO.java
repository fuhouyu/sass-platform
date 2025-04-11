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

import com.fuhouyu.framework.common.annotations.ParamErrorResponse;
import com.fuhouyu.sass.platform.system.domain.dto.BaseDTO;
import com.fuhouyu.sass.platform.system.enums.response.RoleResponseStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;


/**
 * <p>
 * 角色dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 20:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "RoleDTO", description = "角色dto对象")
public class RoleDTO extends BaseDTO {

    @Schema(name = "id", description = "主键id，仅返回")
    private Long id;

    @Schema(name = "roleName", description = "角色名称")
    @NotEmpty(message = "角色名称未填写")
    @ParamErrorResponse(using = RoleResponseStatusEnum.class,
            value = "ROLE_NAME_NOT_NULL")
    private String roleName;

    @Schema(name = "roleCode", description = "角色编码")
    @NotEmpty(message = "角色编码未填写")
    @ParamErrorResponse(using = RoleResponseStatusEnum.class,
            value = "ROLE_CODE_NOT_NULL")
    private String roleCode;

    @Schema(name = "displayOrder", description = "显示顺序")
    @NotNull(message = "显示顺序未填写")
    @ParamErrorResponse(using = RoleResponseStatusEnum.class,
            value = "DISPLAY_CODE_NOT_NULL")
    private Integer displayOrder;

    @Schema(name = "dataScope", description = "数据权限")
    @NotEmpty(message = "数据权限未填写")
    @ParamErrorResponse(using = RoleResponseStatusEnum.class,
            value = "DATA_SCOPE_NOT_NULL")
    private String dataScope;

    @Schema(name = "isEnabled", description = "状态：true 启用")
    @NotNull
    @ParamErrorResponse(using = RoleResponseStatusEnum.class,
            value = "STATUS_NOT_NULL")
    private Boolean isEnabled;

    @Schema(name = "permissionIds", description = "权限id集合")
    @NotEmpty(message = "权限未选择")
    @ParamErrorResponse(using = RoleResponseStatusEnum.class,
            value = "PERMISSION_NOT_NULL")
    private Collection<Long> permissionIds;
}
