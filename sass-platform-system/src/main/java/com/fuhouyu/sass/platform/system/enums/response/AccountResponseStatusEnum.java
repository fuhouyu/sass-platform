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
 * 账号响应异常
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 12:18
 */
@Getter
@RequiredArgsConstructor
public enum AccountResponseStatusEnum implements BaseResponseStatus {

    /**
     * 两次输入的密码不一致
     */
    CONFIRM_PASSWORD_VERIFY_FAIL(110101, "两次输入的密码不一致"),

    /**
     * 原密码不正常
     */
    ORIGINAL_PASSWORD_VERIFY_FAIL(110102, "原密码错误"),

    /**
     * 原密码不能为空
     */
    OLD_PASSWORD_NOT_NULL(110103, "原密码不能为空"),

    /**
     * 新密码不能为空
     */
    NEW_PASSWORD_NOT_NULL(110104, "新密码不能为空"),

    /**
     * 确认密码不能为空
     */
    CONFIRM_PASSWORD_NOT_NULL(110105, "确认密码不能为空"),

    /**
     * 密码不能为空
     */
    PASSWORD_NOT_NULL(110106, "密码不能为空"),
    ;

    private final int code;

    private final String message;
}
