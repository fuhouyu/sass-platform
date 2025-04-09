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
 * 第三方平台响应状态码
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 14:07
 */
@Getter
@RequiredArgsConstructor
public enum ThirdPartyPlatformResponseStatusEnum implements BaseResponseStatus {

    /**
     * 小程序登录失败
     */
    WECHAT_APPLET_LOGIN_ERROR(990501, "小程序登录失败"),

    /**
     * 获取小程序token失败
     */
    WECHAT_APPLET_ACCESS_TOKEN_ERROR(990502, "获取小程序token失败"),

    /**
     * 获取小程序手机号失败
     */
    WECHAT_APPLET_PHONE_ERROR(990503, "获取小程序手机号失败"),

    /**
     * WeLink登录失败
     */
    WELINK_LOGIN_ERROR(990504, "WeLink登录失败"),

    /**
     * 获取WeLink token失败
     */
    WELINK_ACCESS_TOKEN_ERROR(990505, "获取WeLink token失败"),
    ;

    private final int code;

    private final String message;
}
