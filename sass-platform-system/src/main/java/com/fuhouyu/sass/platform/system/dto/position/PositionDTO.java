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
package com.fuhouyu.sass.platform.system.dto.position;

import com.fuhouyu.sass.platform.system.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 岗位dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/8 21:11
 */
@Data
@Schema(name = "PositionDTO", description = "岗位dto对象")
@EqualsAndHashCode(callSuper = true)
public class PositionDTO extends BaseDTO {

    @Schema(name = "id", description = "主键id，仅返回", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(name = "positionName", description = "岗位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String positionName;

    @Schema(name = "positionCode", description = "岗位编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String positionCode;

    @Schema(name = "remark", description = "备注")
    private String remark;

}
