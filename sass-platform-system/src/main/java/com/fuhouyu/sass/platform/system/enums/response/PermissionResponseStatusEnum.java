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
 * 权限响应枚举
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 13:32
 */
@Getter
@RequiredArgsConstructor
public enum PermissionResponseStatusEnum implements BaseResponseStatus {

    /**
     * 权限名称不能为空
     */
    PERMISSION_NAME_NOT_NULL(180101, "权限名称不能为空"),

    /**
     * 权限编码不能为空
     */
    PERMISSION_CODE_NOT_NULL(180102, "权限编码不能为空"),
    /**
     * 权限类型不能为空
     */
    PERMISSION_TYPE_NOT_NULL(180103, "权限类型不能为空"),
    /**
     * 状态不能为空
     */
    STATUS_NOT_NULL(180104, "状态不能为空"),
    /**
     * 显示顺序不能为空
     */
    DISPLAY_ORDER_NOT_NULL(180105, "显示顺序不能为空"),

    /**
     * 没有权限操作该用户
     */
    NO_PERMISSION(180301, "没有权限操作"),

    /**
     * 权限编码已存在
     */
    PERMISSION_CODE_ALREADY_EXISTS(180201, "权限编码已存在"),

    /**
     * 父权限不存在
     */
    PARENT_PERMISSION_NOT_FOUND(180501, "父权限不存在"),
    ;

    private final int code;

    private final String message;
}
