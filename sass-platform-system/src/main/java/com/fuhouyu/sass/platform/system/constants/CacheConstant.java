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
package com.fuhouyu.sass.platform.system.constants;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 缓存常量
 * </p>
 *
 * @author fuhouyu
 * @since 2025/1/1 18:23
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheConstant {

    /**
     * 用户token缓存
     */
    public static final String USER_BIND_TOKEN = "open:platform:bind:token:";
}
