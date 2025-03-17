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
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 租户简略信息的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/16 17:53
 */
@Data
@Builder
@Schema(name = "BasicTenantDTO", description = "租户信息")
public class BasicTenantDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2412348612312389123L;

    @Schema(name = "id", description = "租户id")
    private Long id;

    @Schema(name = "tenantCode", description = "租户编码")
    private String tenantCode;

    @Schema(name = "tenantName", description = "租户名称")
    private String tenantName;

    @Schema(name = "icon", description = "icon")
    private Long icon;
}
