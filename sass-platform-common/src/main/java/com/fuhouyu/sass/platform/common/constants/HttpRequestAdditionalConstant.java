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
package com.fuhouyu.sass.platform.common.constants;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * http 请求扩展常量类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/28 16:32
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRequestAdditionalConstant {

    /**
     * 用户附加信息权限
     */
    public static final String USER_ADDITIONAL_INFORMATION_PERMISSIONS = "permissions";

    /**
     * 租户id
     */
    public static final String TENANT_ADDITIONAL_INFORMATION_ID = "tenantId";

    /**
     * 请求位置
     */
    public static final String IP_LOCATION_ADDITIONAL_INFORMATION = "requestLocation";
}
