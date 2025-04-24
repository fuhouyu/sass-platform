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
package com.fuhouyu.sass.platform.system.domain.dto.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 用户登录的账号详情dto对象，
 * 内部认证使用
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/23 18:52
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserAccountDetailDTO extends AccountDTO {

    @Serial
    private static final long serialVersionUID = 1541235787123123139L;

    /**
     * 用户详情的dto对象
     */
    private Object userDetail;
}
