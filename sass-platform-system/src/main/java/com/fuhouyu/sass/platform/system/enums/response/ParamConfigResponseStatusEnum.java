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
 * 参数配置响应枚举
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 13:28
 */
@RequiredArgsConstructor
@Getter
public enum ParamConfigResponseStatusEnum implements BaseResponseStatus {
    /**
     * 参数名称不能为空
     */
    CONFIG_NAME_NOT_NULL(170101, "配置名称不能为空"),

    /**
     * 参数键名不能为空
     */
    CONFIG_KEY_NOT_NULL(170102, "配置键名不能为空"),

    /**
     * 参数键值不能为空
     */
    CONFIG_VALUE_NOT_NULL(170103, "配置键值不能为空"),

    /**
     * 分组不能为空
     */
    GROUP_KEY_NOT_NULL(170104, "分组不能为空"),

    /**
     * 备注长度超过限制
     */
    REMARK_LENGTH_TOO_LONG(170105, "备注长度超过限制"),


    ;

    private final int code;

    private final String message;
}
