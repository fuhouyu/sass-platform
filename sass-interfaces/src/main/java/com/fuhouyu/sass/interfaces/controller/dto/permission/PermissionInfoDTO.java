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
package com.fuhouyu.sass.interfaces.controller.dto.permission;

import com.fuhouyu.sass.interfaces.controller.dto.BaseResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 权限详情的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 16:38
 */
@Schema(name = "PermissionInfoDTO", description = "权限详情的dto对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionInfoDTO extends BaseResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1237681253123113145L;

    @Schema(name = "id", description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(name = "parentId", description = "父级id，一级时为-1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long parentId;

    @Schema(name = "permissionName", description = "权限名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String permissionName;

    @Schema(name = "permissionCode", description = "权限编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String permissionCode;

    @Schema(name = "displayOrder", description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer displayOrder;

    @Schema(name = "icon", description = "icon", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String icon;

    @Schema(name = "routePath", description = "路由路径", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String routePath;

    @Schema(name = "componentPath", description = "组件路径", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String componentPath;

    @Schema(name = "urlParams", description = "url参数", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String urlParams;

    @Schema(name = "isFrame", description = "是否外链", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isFrame;

    @Schema(name = "isAllowModified", description = "是否允许修改", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isAllowModified;

    @Schema(name = "isSystemd", description = "是否系统权限", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isSystemd;

    @Schema(name = "isVisible", description = "是否显示", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isVisible;


}
