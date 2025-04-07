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
package com.fuhouyu.sass.platform.system.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 刷新令牌的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/7 23:28
 */
@Data
@Schema(name = "RefreshTokenDTO", description = "刷新令牌的dto对象")
public class RefreshTokenDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 987123986675123765L;

    @Schema(name = "refreshToken", description = "刷新令牌")
    @NotEmpty(message = "刷新令牌不能为空")
    private String refreshToken;
}
