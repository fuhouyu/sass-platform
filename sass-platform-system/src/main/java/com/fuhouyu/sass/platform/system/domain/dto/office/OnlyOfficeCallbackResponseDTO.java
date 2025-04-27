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
package com.fuhouyu.sass.platform.system.domain.dto.office;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * <p>
 * office 回调dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/26 22:25
 */
@Schema(name = "OnlyOfficeCallbackResponseDTO", description = "office 回调响应的dto对象", hidden = true)
public record OnlyOfficeCallbackResponseDTO(
        @Schema(name = "error", description = "错误响应") Integer error) implements Serializable {

    public static OnlyOfficeCallbackResponseDTO success() {
        return new OnlyOfficeCallbackResponseDTO(0);
    }
}
