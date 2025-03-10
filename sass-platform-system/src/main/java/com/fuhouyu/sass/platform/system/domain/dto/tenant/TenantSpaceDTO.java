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

import com.fuhouyu.sass.platform.system.domain.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 租户空间dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/17 21:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "TenantSpaceDTO", description = "租户空间dto对象")
public class TenantSpaceDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 623454982345482349L;

    @Schema(name = "tenantId", description = "租户id")
    private Long tenantId;

    @Schema(name = "bucketName", description = "存储桶名称")
    @NotEmpty(message = "存储桶名未输入")
    private String bucketName;

    @Schema(name = "capacity", description = "容量，单位G")
    private Long capacity;

    @Schema(name = "acl", description = "存储桶权限")
    @NotEmpty(message = "权限未输入")
    private String acl;
}
