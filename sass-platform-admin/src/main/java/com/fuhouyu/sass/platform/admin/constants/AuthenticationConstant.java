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
package com.fuhouyu.sass.platform.admin.constants;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 认证常量
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/4 19:09
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationConstant {


    /**
     * cache 用户登录错误次数
     */
    public static final String CACHE_USER_LOGIN_ERROR_PREFIX = "user:login:error:count:";

    /**
     * cache 登录错误的参数前缀
     */
    public static final String CACHE_LOGIN_ERROR_PARAM_PREFIX = "login:error:param";

    /**
     * 登录错误标识
     */
    public static final String LOGIN_ERROR_GROUP_KEY = "LOGIN_ERROR";

    /**
     * 登录失败警告次数限制 key
     */
    public static final String LOGIN_FAIL_WARNING_THRESHOLD_KEY = "LOGIN_FAIL_WARNING_THRESHOLD";

    /**
     * 默认登录失败警告次数限制
     */
    public static final String DEFAULT_LOGIN_FAIL_WARNING_THRESHOLD_VALUE = "3";

    /**
     * 登录错误警告信息
     */
    public static final String LOGIN_FAIL_WARNING_MESSAGE_KEY = "LOGIN_FAIL_WARNING_MESSAGE";

    /**
     * 默认的登录错误警告信息
     */
    public static final String DEFAULT_LOGIN_FAIL_WARNING_MESSAGE_VALUE = "您已尝试%s次登录失败，%s次后将被锁定！";

    /**
     * 登录失败错误提示
     */

    public static final String LOGIN_FAIL_ERROR_MESSAGE_KEY = "LOGIN_FAIL_ERROR_MESSAGE";

    /**
     * 默认的登录失败错误提示值
     */
    public static final String DEFAULT_LOGIN_FAIL_ERROR_MESSAGE_VALUE = "用户名或密码错误，请重新输入";

    /**
     * 登录失败最大尝试次数
     */
    public static final String LOGIN_FAIL_MAX_ATTEMPTS_KEY = "LOGIN_FAIL_MAX_ATTEMPTS";

    /**
     * 默认的登录失败最大尝试次数
     */
    public static final String DEFAULT_LOGIN_FAIL_MAX_ATTEMPTS_VALUE = "5";

    /**
     * 登录失败锁定时长
     */
    public static final String LOGIN_FAIL_LOCK_DURATION_KEY = "LOGIN_FAIL_LOCK_DURATION";

    /**
     * 默认登录失败锁定时长
     */
    public static final String DEFAULT_LOGIN_FAIL_LOCK_DURATION_VALUE = "15";

    /**
     * 账号已被锁定提示
     */
    public static final String LOGIN_ACCOUNT_LOCKED_MESSAGE_KEY = "LOGIN_ACCOUNT_LOCKED_MESSAGE";

    /**
     * 默认的账号已被锁定提示
     */
    public static final String DEFAULT_ACCOUNT_LOCKED_MESSAGE_VALUE = "您的账号已被锁定，请在%s分钟后重试！";
}
