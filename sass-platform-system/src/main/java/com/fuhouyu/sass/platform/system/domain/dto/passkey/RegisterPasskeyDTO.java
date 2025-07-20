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
package com.fuhouyu.sass.platform.system.domain.dto.passkey;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 注册通行密钥
 * </p>
 *
 * @author fuhouyu
 * @since 2025/7/19 22:57
 */
@Schema(name = "RegisterPasskeyDTO", description = "通行证密钥注册dto对象")
@Getter
@Setter
@ToString
public class RegisterPasskeyDTO implements Serializable {

    @Schema(description = "通行密钥名称")
    @NotEmpty(message = "通行密钥名称未输入")
    private String passkeyName;

    @Schema(description = "通行密钥注册响应的json对象")
    @NotEmpty(message = "通行密钥数据未输入")
    private String registrationResponse;
}
