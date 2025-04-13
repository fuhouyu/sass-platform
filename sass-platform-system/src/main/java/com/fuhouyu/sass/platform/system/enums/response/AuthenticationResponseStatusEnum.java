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
 * 认证的异常响应
 * 参数校验错误	01
 * 业务逻辑冲突	02
 * 权限不足	03
 * 系统异常	05
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 10:54
 */
@Getter
@RequiredArgsConstructor
public enum AuthenticationResponseStatusEnum implements BaseResponseStatus {

    /**
     * 账号不能为空
     */
    ACCOUNT_NOT_NULL(100101, "账号不能为空"),

    /**
     * 账号类型不能为空
     */
    ACCOUNT_TYPE_NOT_NULL(100102, "账号类型不能为空"),

    /**
     * 租户id不能为空
     */
    TENANT_ID_NOT_NULL(100103, "租户ID不能为空"),

    /**
     * 当前账号不属于该租户
     */
    USER_NOT_IN_TENANT(100104, "当前账号不属于该租户"),


    /**
     * 用户绑定信息已过期
     */
    THIRD_PARTY_ACCOUNT_BIND_EXPIRE(100201, "用户绑定信息已过期，请重新登录"),

    /**
     * 当前第三方账号已绑定
     */
    THIRD_PARTY_ACCOUNT_BINDING(100202, "当前第三方账号已绑定"),

    /**
     * 当前WeLink用户未绑定账号，请先进行绑定账号后操作
     */
    WELINK_NOT_BIND(100203, "当前WeLink用户未绑定账号，请先进行绑定账号后操作"),

    /**
     * 验证码验证失败
     */
    CLOUDFLARE_TURNSTILE_VERIFY_FAIL(100204, "验证失败，请重新验证"),

    /**
     * 当前账号被禁用
     */
    USER_ACCOUNT_DISABLED(100301, "当前账号被禁用"),
    ;


    private final int code;

    private final String message;


}
