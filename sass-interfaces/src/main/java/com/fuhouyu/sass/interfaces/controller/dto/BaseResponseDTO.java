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
package com.fuhouyu.sass.interfaces.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 基类dto响应
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 16:55
 */
@Schema(name = "BaseResponseDTO", description = "基类响应dto对象")
@Data
public class BaseResponseDTO implements Serializable {

    @Schema(name = "createAt", description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createAt;

    @Schema(name = "updateAt", description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateAt;

    @Schema(name = "createBy", description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String createBy;

    @Schema(name = "updateBy", description = "操作人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String updateBy;
}
