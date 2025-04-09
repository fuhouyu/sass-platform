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
 * 角色接口
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 13:51
 */
@RequiredArgsConstructor
@Getter
public enum RoleResponseStatusEnum implements BaseResponseStatus {

    /**
     * 角色名称不能为空
     */
    ROLE_NAME_NOT_NULL(210101, "角色名称不能为空"),

    /**
     * 角色编码不能为空
     */
    ROLE_CODE_NOT_NULL(210102, "角色编码不能为空"),

    /**
     * 显示顺序不能为空
     */
    DISPLAY_CODE_NOT_NULL(210103, "显示顺序不能为空"),

    /**
     * 数据范围不能为空
     */
    DATA_SCOPE_NOT_NULL(210104, "数据范围不能为空"),

    /**
     * 状态不能为空
     */
    STATUS_NOT_NULL(210105, "状态不能为空"),

    /**
     * 权限不能为空
     */
    PERMISSION_NOT_NULL(210106, "权限不能为空"),

    /**
     * 权限编码已存在
     */
    ROLE_CODE_ALREADY_EXISTS(210201, "角色编码已存在"),

    /**
     * 角色不存在
     */
    ROLE_NOT_EXISTS(210501, "角色不存在"),
    ;

    private final int code;

    private final String message;
}
