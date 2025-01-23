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
package com.fuhouyu.sass.platform.system.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 修改密码的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/1 16:24
 */
@Data
@Schema(name = "UpdatePasswordDTO", description = "修改密码的dto对象")
public class UpdatePasswordDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5712354211236541231L;

    @Schema(name = "oldPassword", description = "旧密码")
    @NotEmpty(message = "旧密码未输入")
    private String oldPassword;

    @Schema(name = "newPassword", description = "新密码")
    @NotEmpty(message = "新密码密码未输入")
    private String newPassword;

    @Schema(name = "confirmPassword", description = "确认密码")
    @NotEmpty(message = "确认密码未输入")
    private String confirmPassword;
}
