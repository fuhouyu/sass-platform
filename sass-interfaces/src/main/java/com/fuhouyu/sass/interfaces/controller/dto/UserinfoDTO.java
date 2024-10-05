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
package com.fuhouyu.sass.interfaces.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 19:05
 */
@Data
@Schema(name = "UserinfoDTO", description = "用户详情dto对象")
public class UserinfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1238912361L;

    @Schema(name = "id", description = "主键id")
    private Long id;

    @Schema(name = "username", description = "用户名")
    private String username;

    @Schema(name = "realName", description = "真实姓名")
    private String realName;

    @Schema(name = "id", description = "主键id")
    private String nickname;

    @Schema(name = "email", description = "邮件地址")
    private String email;

    @Schema(name = "gender", description = "性别")
    private String gender;

    @Schema(name = "avatar", description = "头像地址")
    private String avatar;

    @Schema(name = "loginDate", description = "登录时间")
    private LocalDateTime loginDate;

    @Schema(name = "loginIp", description = "登录ip")
    private String loginIp;


}
