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

/**
 * <p>
 * matomo enum
 * </p>
 *
 * @author fuhouyu
 * @since 2025/5/4 19:48
 */
public enum MatomoPeriodEnum {

    /**
     * 天
     */
    DAY,

    /**
     * 周
     */
    WEEK,

    /**
     * 月
     */
    MONTH,

    ;


    /**
     * 获取枚举的名称
     *
     * @return 枚举名称
     */
    public String toLowerCase() {
        return this.name().toLowerCase();
    }
}
