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
package com.fuhouyu.sass.platform.system.enums;

import com.fuhouyu.framework.common.response.BaseResponseStatus;

/**
 * <p>
 * web 响应枚举
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/1 19:15
 */
public enum WebResponseStatusEnum implements BaseResponseStatus {

    /**
     * 用户未绑定
     */
    USER_NOT_BIND(1001, "用户未绑定"),
    ;

    private final int code;

    private final String message;

    WebResponseStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
