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
package com.fuhouyu.sass.platform.system.constants;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * http 请求头常量
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/1 18:20
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRequestHeaderConstant {

    /**
     * 用户bind token
     */
    public static final String USER_BIND = "X-User-Bind";

    /**
     * 用户第三方账号登录时的临时token
     */
    public static final String USER_BIND_TOKEN = "X-User-Bind-Temporary-Token";
}
