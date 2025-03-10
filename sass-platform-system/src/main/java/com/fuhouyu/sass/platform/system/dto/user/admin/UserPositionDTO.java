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
package com.fuhouyu.sass.platform.system.dto.user.admin;

import com.fuhouyu.sass.platform.system.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 用户职位dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/11 10:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "UserPositionDTO", description = "用户职位dto对象")
public class UserPositionDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 3918230818274786123L;

    @Schema(name = "organizationId", description = "组织id")
    @NotNull(message = "组织未选择")
    private Long organizationId;

    @Schema(name = "userId", description = "用户id")
    private Long userId;

    @Schema(name = "positionName", description = "职位名称")
    @NotEmpty(message = "职位未填写")
    private String positionName;

    @Schema(name = "isMain", description = "是否为主职，一个用户只能有一个主职，参考weLink")
    @NotNull(message = "是否主职未选择")
    private Boolean isMain;

    @Schema(name = "orderInOrganization", description = "当前用户在组织内的排序")
    @NotNull(message = "用户组织内排序未填写")
    private Long orderInOrganization;

}
