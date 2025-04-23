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
package com.fuhouyu.sass.platform.system.domain.dto.user.admin;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fuhouyu.framework.log.serializer.LogRequestParamDesensitizeSerializer;
import com.fuhouyu.sass.platform.system.domain.dto.account.AccountIdDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 用户登录操作
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/4 22:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "UserLoginDTO", description = "用户登录的dto对象")
public class UserLoginDTO extends AccountIdDTO {

    @Serial
    private static final long serialVersionUID = 23908102938012983L;

    @Schema(name = "credentials", description = """
            登录凭证
            """, requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonSerialize(using = LogRequestParamDesensitizeSerializer.class)
    private String credentials;

    @Schema(name = "cloudflareTurnstileToken", description = """
            cloudflare 令牌，类型为password时，需要验证
            """, requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonSerialize(using = LogRequestParamDesensitizeSerializer.class)
    private String cloudflareTurnstileToken;
}
