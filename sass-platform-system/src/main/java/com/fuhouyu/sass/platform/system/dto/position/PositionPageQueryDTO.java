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

import com.fuhouyu.sass.platform.system.dto.page.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 岗位查询的dto
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/8 21:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "PositionPageQueryDTO", description = "岗位查询的dto对象")
public class PositionPageQueryDTO extends PageQueryDTO {

    @Schema(name = "positionName", description = "岗位模糊查询")
    private String positionName;

    @Schema(name = "positionCode", description = "岗位编码")
    private String positionCode;

}
