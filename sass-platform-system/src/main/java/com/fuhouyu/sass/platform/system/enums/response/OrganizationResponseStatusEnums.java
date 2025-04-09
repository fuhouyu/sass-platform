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
 * 组织响应枚举
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 13:21
 */
@RequiredArgsConstructor
@Getter
public enum OrganizationResponseStatusEnums implements BaseResponseStatus {

    /**
     * 组织名称不能为空
     */
    ORGANIZATION_NAME_NOT_NULL(160101, "组织名称不能为空"),

    /**
     * 组织编码不能为空
     */
    ORGANIZATION_CODE_NOT_NULL(160102, "组织名称不能为空"),

    /**
     * 组织类型不能为空
     */
    ORGANIZATION_TYPE_NOT_NULL(160103, "组织类型不能为空"),

    /**
     * 状态不能为空
     */
    STATUS_NOT_NULL(160104, "状态不能为空"),

    /**
     * 显示顺序不能为空
     */
    DISPLAY_ORDER_NOT_NULL(160105, "显示顺序不能为空"),

    /**
     * 备注长度超过限制
     */
    REMARK_LENGTH_TOO_LONG(160106, "备注长度超过限制"),


    /**
     * 组织编码已存在
     */
    ORGANIZATION_CODE_EXISTS(160202, "组织编码已存在"),

    /**
     * 上级组织不存在
     */
    PARENT_ORGANIZATION_NOT_EXISTS(160501, "上级组织不存在"),
    ;


    private final int code;

    private final String message;
}
