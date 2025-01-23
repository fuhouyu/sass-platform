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

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fuhouyu.sass.platform.system.enums.AccountTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 账号id实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/2 19:05
 */
@Schema(name = "AccountIdDTO", description = "账号id cto对象")
@Data
public class AccountIdDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = 5142354211238415241L;

    /**
     * 用户名
     */
    @NotBlank(message = "登录标识未填写")
    @Schema(name = "account", description = """
            登录标识，如用户名，刷新令牌等
            """, requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonAlias({"account", "identify", "username"})
    private String account;
    /**
     * 账号类型
     */
    @NotNull(message = "登录类型未选择")
    @Schema(name = "accountType", description = "账号类型", defaultValue = "PASSWORD", requiredMode = Schema.RequiredMode.REQUIRED)
    private AccountTypeEnum accountType;

    public AccountIdDTO() {
    }

    public AccountIdDTO(String account, AccountTypeEnum accountType) {
        this.account = account;
        this.accountType = accountType;
    }
}
