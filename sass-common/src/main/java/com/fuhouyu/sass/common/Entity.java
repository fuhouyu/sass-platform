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
package com.fuhouyu.sass.common;

import java.io.Serializable;

/**
 * <p>
 * 实体接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/24 16:49
 */
public interface Entity<ID> extends Serializable {

    /**
     * 获取唯一标识符id
     *
     * @return 标识id
     */
    ID getIdentifierId();

    /**
     * 判断实体中的标识是否一致
     *
     * @param other 标识符
     * @return 标识符相同时返回true
     */
    boolean sameIdentityAs(ID other);
}
