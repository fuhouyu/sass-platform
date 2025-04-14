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
 * 租户响应状态枚举
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 13:59
 */
@Getter
@RequiredArgsConstructor
public enum TenantResponseStatusEnum implements BaseResponseStatus {

    /**
     * 租户名称不能为空
     */
    TENANT_NAME_NOT_NULL(220101, "租户名称不能为空"),

    /**
     * 租户编码不能为空
     */
    TENANT_CODE_NOT_NULL(220102, "租户编码不能为空"),

    /**
     * 租户编码已存在
     */
    TENANT_CODE_ALREADY_EXISTS(220201, "租户编码已存在"),

    /**
     * 没有权限操作该租户
     */
    TENANT_NO_PERMISSION(220301, "没有权限操作该租户"),

    /**
     * 租户编码不存在
     */
    TENANT_NOT_EXISTS(220501, "租户不存在"),
    ;

    private final int code;

    private final String message;
}
