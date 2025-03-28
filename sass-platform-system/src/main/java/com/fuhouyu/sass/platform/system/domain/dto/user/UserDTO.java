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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuhouyu.sass.platform.system.domain.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/10 12:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "userDTO", description = "用户dto对象")
@Builder
public class UserDTO extends BaseDTO {

    @Schema(name = "id", description = "用户id")
    private Long id;

    @Schema(name = "nickname", description = "昵称")
    private String nickname;

    @Schema(name = "id", description = "手机号")
    private String phone;

    @Schema(name = "gender", description = "性别")
    private String gender;

    @Schema(name = "avatar", description = "头像")
    private String avatar;

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "birthday", description = "生日")
    private LocalDate birthday;

    @Schema(name = "loginDate", description = "登录日期")
    private LocalDateTime loginDate;

    @Schema(name = "ownerTenantId", description = "用户当前登录的租户id，仅返回", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("tenantId")
    private Long ownerTenantId;

    @Schema(name = "loginIp", description = "登录ip")
    private String loginIp;

    @Schema(name = "isEnabled", description = "是否启用")
    private Boolean isEnabled;
}
