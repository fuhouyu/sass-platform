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
package com.fuhouyu.sass.platform.system.dto.user;

import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>
 * 用户登录操作
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/4 22:00
 */
@Data
@Schema(name = "UserLoginDTO", description = "用户登录的dto对象")
public class UserLoginDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "登录标识未填写")
    @Schema(name = "identify", description = """
            登录标识，如用户名，刷新令牌等
            """, requiredMode = Schema.RequiredMode.REQUIRED)
    private String identify;

    /**
     * 账号类型
     */
    @NotNull(message = "登录类型未选择")
    @Schema(name = "accountType", description = "账号类型", defaultValue = "PASSWORD", requiredMode = Schema.RequiredMode.REQUIRED)
    private AccountTypeEnum accountType;

    /**
     * 凭证
     */
    @Schema(name = "credentials", description = """
            登录凭证
            """, requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String credentials;



}
