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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * <p>
 * 用户token DTO对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 12:23
 */
@Getter
@Setter
@ToString
@Schema(name = "UserTokenDTO", description = "用户token 对象")
public class UserTokenDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1864123128912356412L;

    @Schema(name = "accessTokenExpireSeconds", description = "认证令牌签发时长，单位：秒")
    private long accessTokenExpireSeconds;

    @Schema(name = "refreshTokenExpireSeconds", description = "刷新令牌签发时长，单位：秒")
    private long refreshTokenExpireSeconds;

    @Schema(name = "tokenType", description = "token类型，一般为bearer")
    private String tokenType;

    @Schema(name = "accessToken", description = "认证令牌")
    private String accessToken;

    @Schema(name = "refreshToken", description = "刷新令牌")
    private String refreshToken;

    @Schema(name = "accessTokenIssuedAt", description = "认证令牌签发时间")
    private Instant accessTokenIssuedAt;

    @Schema(name = "refreshTokenIssuedAt", description = "刷新令牌签发时间")
    private Instant refreshTokenIssuedAt;

    @Schema(name = "accessTokenExpireAt", description = "认证令牌过期时间")
    private Instant accessTokenExpireAt;

    @Schema(name = "refreshTokenExpireAt", description = "刷新令牌过期时间")
    private Instant refreshTokenExpireAt;
}
