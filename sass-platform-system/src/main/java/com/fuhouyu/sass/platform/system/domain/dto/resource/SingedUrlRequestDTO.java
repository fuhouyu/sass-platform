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
package com.fuhouyu.sass.platform.system.domain.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.Duration;

/**
 * <p>
 * 签名url请求的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/24 21:16
 */
@Data
@Schema(name = "SingedUrlRequestDTO", description = "签名url请求的dto对象")
public class SingedUrlRequestDTO implements Serializable {

    @Schema(name = "isPreview", description = "是否预览")
    private Boolean isPreview;

    @Schema(name = "expires", description = "过期时间", example = "600")
    private Long expires = Duration.ofMinutes(10).getSeconds();
}
