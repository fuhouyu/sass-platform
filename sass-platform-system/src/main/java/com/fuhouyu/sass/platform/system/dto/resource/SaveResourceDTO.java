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
package com.fuhouyu.sass.platform.system.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 保存资源的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/22 23:07
 */
@Data
@Schema(name = "SaveResourceDTO", description = "保存资源的dto对象")
@EqualsAndHashCode(callSuper = true)
public class SaveResourceDTO extends ResourceDTO {

    @Serial
    private static final long serialVersionUID = 8917239667152376512L;

    @Schema(name = "businessName", description = "业务名称")
    @NotEmpty(message = "业务未填写")
    private String businessName;
}
