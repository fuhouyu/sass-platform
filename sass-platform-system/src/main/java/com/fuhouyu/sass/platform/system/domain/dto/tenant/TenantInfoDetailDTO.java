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

/**
 * <p>
 * 租户详情的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/5 21:07
 */
@Schema(name = "TenantInfoDetailDTO", description = "租户详情dto对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantInfoDetailDTO extends TenantInfoDTO {

    @Serial
    private static final long serialVersionUID = -8912378681465128376L;

    @Schema(name = "adminUserRealName", description = "管理员用户账号真实姓名, 仅返回")
    private String adminUserRealName;

    @Schema(name = "tenantSpace", description = "租户空间信息")
    @Valid
    private TenantSpaceDTO tenantSpace;
}
