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
 * 字典类型响应枚举
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 12:54
 */
@Getter
@RequiredArgsConstructor
public enum DictTypeResponseStatusEnum implements BaseResponseStatus {

    /**
     * 字典类型名称不能为空
     */
    DICT_TYPE_NAME_NOT_NULL(130101, "字典类型名称不能为空"),

    /**
     * 字典类型编码不能为空
     */
    DICT_TYPE_CODE_NOT_NULL(130102, "字典类型编码不能为空"),


    /**
     * 状态不能为空
     */
    STATUS_NOT_NULL(130103, "状态不能为空"),

    /**
     * 显示顺序不能为空
     */
    DISPLAY_ORDER_NOT_NULL(130104, "显示顺序不能为空"),


    /**
     * 备注长度超过限制
     */
    REMARK_LENGTH_TOO_LONG(130105, "备注长度超过限制"),


    /**
     * 字典类型编码已存在
     */
    DICT_TYPE_CODE_EXISTS(130201, "字典类型编码已存在"),

    /**
     * 字典类型不能为空
     */
    DICT_TYPE_NOT_EXISTS(130501, "字典类型编码不存在"),
    ;
    private final int code;

    private final String message;
}
