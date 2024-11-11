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

import com.fuhouyu.sass.platform.system.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 19:05
 */
@Getter
@Setter
@ToString
@Schema(name = "UserDTO", description = "用户详情dto对象")
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 1238912361L;

    @Schema(name = "id", description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(name = "username", description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,20}$",
            message = "用户名格式不正确，必须以字母开头，并使用3到20个字符，仅包含字母、数字和下划线。")
    private String username;

    @Schema(name = "realName", description = "真实姓名")
    private String realName;

    @Schema(name = "nickname", description = "昵称")
    private String nickname;

    @Schema(name = "email", description = "邮件地址")
    private String email;

    @Schema(name = "gender", description = "性别")
    private String gender;

    @Schema(name = "avatar", description = "头像地址")
    private String avatar;

    @Schema(name = "loginDate", description = "登录时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime loginDate;

    @Schema(name = "loginIp", description = "登录ip", requiredMode = Schema.RequiredMode.REQUIRED)
    private String loginIp;


}
