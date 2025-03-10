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
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 租户空间详情dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/26 21:32
 */
@Schema(name = "TenantSpaceDetailDTO", description = "租户空间详情dto对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantSpaceDetailDTO extends TenantSpaceDTO {

    @Serial
    private static final long serialVersionUID = -641235481235489123L;

    @Schema(name = "usedCapacity", description = "已使用容量")
    private Long usedCapacity;
}
