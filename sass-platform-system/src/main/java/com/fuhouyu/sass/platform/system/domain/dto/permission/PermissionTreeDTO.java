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

import com.fuhouyu.sass.platform.common.BaseTree;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 权限详情树dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 17:05
 */
@Schema(name = "PermissionTreeDTO", description = "权限树详情dto对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionTreeDTO extends PermissionDTO implements BaseTree<PermissionTreeDTO> {

    @Schema(name = "children", description = "权限树子集", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<PermissionTreeDTO> children;

}
