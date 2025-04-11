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
 * 应用响应枚举类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 19:48
 */
@Getter
@RequiredArgsConstructor
public enum ApplicationResponseStatusEnum implements BaseResponseStatus {

    /**
     * 客户端ID不能为空
     */
    CLIENT_ID_NOT_NULL(230101, "客户端ID不能为空"),

    /**
     * 客户端密钥不能为空
     */
    CLIENT_SECRET_NOT_NULL(230102, "客户端密钥不能为空"),

    /**
     * 客户端名称不能为空
     */
    CLIENT_NAME_NOT_NULL(230103, "客户端名称不能为空"),

    /**
     * 客户端回调地址不能为空
     */
    CLIENT_REDIRECT_URL_NOT_NULL(230104, "客户端回调地址不能为空"),

    /**
     * 客户端授权类型不能为空
     */
    CLIENT_GRANT_TYPE_NOT_NULL(230105, "客户端授权类型不能为空"),

    /**
     * 客户端访问令牌有效时间不能为空
     */
    CLIENT_ACCESS_TOKEN_VALIDITY_NOT_NULL(230106, "客户端访问令牌有效时间不能为空"),

    /**
     * 客户端刷新令牌有效时间不能为空
     */
    CLIENT_REFRESH_TOKEN_VALIDITY_NOT_NULL(230107, "客户端刷新令牌有效时间不能为空"),

    /**
     * 客户端状态不能为空
     */
    CLIENT_STATUS_NOT_NULL(230108, "客户端状态不能为空"),

    ;

    private final int code;

    private final String message;


}
