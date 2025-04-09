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
package com.fuhouyu.sass.platform.system.enums.response;

import com.fuhouyu.framework.common.response.BaseResponseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 用户响应枚举类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 09:50
 */
@RequiredArgsConstructor
@Getter
public enum UserResponseStatusEnum implements BaseResponseStatus {
    /**
     * 用户名不能为空
     */
    USERNAME_NOT_NULL(120101, "用户名不能为空"),

    /**
     * 用户名已存在
     */
    USER_ALREADY_EXISTS(120201, "用户名已存在"),

    /**
     * 没有权限操作该用户
     */
    USER_NO_PERMISSION(120301, "没有权限操作该用户"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(120502, "用户不存在"),

    /**
     * 用户注册失败
     */
    USER_REGISTER_ERROR(120501, "用户注册失败"),


    ;

    private final int code;

    private final String message;
}
