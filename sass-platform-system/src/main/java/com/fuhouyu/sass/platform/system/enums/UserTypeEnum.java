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
package com.fuhouyu.sass.platform.system.enums;

import java.util.Locale;
import java.util.Objects;

/**
 * <p>
 * 用户类型
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/10 17:46
 */
public enum UserTypeEnum {
    /**
     * 管理员用户
     */
    ADMIN,
    /**
     * 普通用户
     */
    NORMAL,

    ;


    /**
     * 判断是否是管理员账号
     *
     * @param userType 用户类型
     * @return true / false
     */
    public static boolean isAdmin(String userType) {
        return Objects.nonNull(userType) &&
                Objects.equals(userType.toUpperCase(Locale.ROOT), ADMIN.name());
    }
}
