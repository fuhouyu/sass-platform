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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户登录的详情对象
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/20 22:00
 */
@Data
@Schema(name = "LoginUserDetailDTO", description = "用户登录的详情dto对象")
public class LoginUserDetailDTO implements Serializable {

    @Schema(name = "loginTime", description = "登录时间")
    private LocalDateTime loginTime;

    @Schema(name = "loginIp", description = "登录ip")
    private String loginIp;

    @Schema(name = "loginLocation", description = "登录地点")
    private String loginLocation;

    @Schema(name = "loginTenantId", description = "登录的租户id")
    private Long loginTenantId;

    @Schema(name = "loginType", description = "登录类型")
    private String loginType;

    @Schema(name = "clientId", description = "登录的客户端id，如果登录的租户后台，则该值为null")
    private String clientId;

    @Schema(name = "os", description = "操作系统")
    private String os;

    @Schema(name = "browser", description = "浏览器")
    private String browser;

    @Schema(name = "browserVersion", description = "浏览器版本")
    private String browserVersion;

    @Schema(name = "engine", description = "engine")
    private String engine;

    @Schema(name = "platform", description = "平台")
    private String platform;

}
