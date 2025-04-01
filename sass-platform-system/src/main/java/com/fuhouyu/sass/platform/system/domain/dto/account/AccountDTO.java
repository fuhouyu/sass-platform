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
package com.fuhouyu.sass.platform.system.domain.dto.account;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fuhouyu.framework.log.serializer.LogRequestParamDesensitizeSerializer;
import com.fuhouyu.sass.platform.system.domain.dto.BaseDTO;
import com.fuhouyu.sass.platform.system.domain.dto.ValidGroups;
import com.fuhouyu.sass.platform.system.enums.UserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * <p>
 * 账号实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/27 17:54
 */
@Getter
@Setter
@ToString
@Schema(name = "AccountDTO", description = "账号dto对象")
public class AccountDTO extends BaseDTO {


    @Schema(name = "account", description = "账号")
    private String account;

    @Schema(name = "accountType", description = "账号类型")
    private String accountType;

    @Schema(name = "userId", description = "用户id")
    private Long userId;

    @JsonProperty("credentials")
    @JsonAlias({"password", "credentials"})
    @NotEmpty(message = "用户密码未填写", groups = ValidGroups.SaveGroup.class)
    @JsonSerialize(using = LogRequestParamDesensitizeSerializer.class)
    private String credentials;

    @JsonIgnore
    private LocalDateTime credentialsExpirationTime;

    @Schema(name = "refAccountId", description = "第三方所属的账号id")
    private String refAccountId;

    @Schema(name = "isEnabled", description = "是否启用标记")
    private Boolean isEnabled;

    @Schema(name = "用户类型", description = "用户类型")
    private UserTypeEnum userType;

    @Schema(name = "ownerTenantId", description = "所属的租户id")
    private Long ownerTenantId;

}
