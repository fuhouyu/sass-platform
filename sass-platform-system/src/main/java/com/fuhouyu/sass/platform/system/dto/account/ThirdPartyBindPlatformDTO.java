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
package com.fuhouyu.sass.platform.system.dto.account;

import com.fuhouyu.sass.platform.system.dto.user.UserLoginDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 第三方账号绑定的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/1 19:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ThirdPartyBindPlatformDTO", description = "第三方平台绑定的dto对象")
public class ThirdPartyBindPlatformDTO extends UserLoginDTO {

    @Serial
    private static final long serialVersionUID = 4123545123215612351L;


    @Schema(name = "temporaryToken", description = "临时token")
    @NotEmpty(message = "临时token未填写")
    private String temporaryToken;

}
